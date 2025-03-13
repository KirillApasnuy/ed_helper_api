package ru.bezfy.ed_helper_api.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.bezfy.ed_helper_api.api.model.AuthorizationModel;
import ru.bezfy.ed_helper_api.exception.EmailFailureException;
import ru.bezfy.ed_helper_api.exception.InvalidCredentialsException;
import ru.bezfy.ed_helper_api.exception.UserAlreadyExistsException;
import ru.bezfy.ed_helper_api.exception.UserNotFoundException;
import ru.bezfy.ed_helper_api.model.LocalUser;
import ru.bezfy.ed_helper_api.model.VerificationToken;
import ru.bezfy.ed_helper_api.model.dao.LocalUserDAO;
import ru.bezfy.ed_helper_api.model.dao.VerificationTokenDAO;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Optional;

@Service
public class AuthService {
    private static final SecureRandom random = new SecureRandom();

    final LocalUserDAO localUserDAO;
    final EncryptionService encryptionService;
    final JWTService jwtService;
    final EmailService emailService;
    final VerificationTokenDAO verificationTokenDAO;

    public AuthService(LocalUserDAO localUserDAO, EncryptionService encryptionService, JWTService jwtService, EmailService emailService, VerificationTokenDAO verificationTokenDAO) {
        this.localUserDAO = localUserDAO;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationTokenDAO = verificationTokenDAO;
    }

    public void register(AuthorizationModel authModel) throws UserAlreadyExistsException, EmailFailureException {
        // Проверяем, существует ли пользователь с таким email
        System.out.println(authModel.toString());
        if (localUserDAO.findByEmail(authModel.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        // Создаем нового пользователя
        LocalUser newUser = new LocalUser();
        newUser.setEmail(authModel.getEmail());
        newUser.setUploadFolderId(authModel.getUploadFolderId());
        newUser.setReceiveEmail(authModel.getReceivedEmail());
        newUser.setEmailVerified(false);
        String encryptedPassword = encryptionService.encryptPassword(authModel.getPassword());
        newUser.setPassword(encryptedPassword);
        VerificationToken verificationToken = createVerificationToken(newUser);
        try {

        emailService.sendVerificationEmail(verificationToken);
        } catch (Exception ignored) {

        }

        localUserDAO.save(newUser);
        System.out.println("save user");
    }

    public String login(AuthorizationModel authModel) throws UserNotFoundException, InvalidCredentialsException {
        Optional<LocalUser> opUser = localUserDAO.findByEmail(authModel.getEmail());
        if (opUser.isPresent()) {
            final LocalUser user = opUser.get();
            if (encryptionService.verifyPassword(authModel.getPassword(), user.getPassword())){
                return jwtService.generateJWT(user);
            };
            throw new InvalidCredentialsException();
        }
        throw new UserNotFoundException();
    }

    @Transactional
    public String verifyUser(String token) throws InvalidCredentialsException {
        Optional<VerificationToken> opToken = verificationTokenDAO.findByToken(token);
        if (opToken.isPresent()) {
            VerificationToken verificationToken = opToken.get();
            LocalUser user = verificationToken.getUser();
            if (!user.getEmailVerified()) {
                user.setEmailVerified(true);
                localUserDAO.save(user);
                verificationTokenDAO.deleteByUser(user);
                return jwtService.generateJWT(user);
            }
        }
        throw new InvalidCredentialsException();
    }

    private VerificationToken createVerificationToken(LocalUser user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(generateRandomCode());
        verificationToken.setCreated(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

    private String generateRandomCode() {
        int code = 100000 + random.nextInt(900000); // Генерация числа от 100000 до 999999
        return String.valueOf(code);
    }
}