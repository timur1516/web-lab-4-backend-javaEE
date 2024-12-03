package ru.timur.web4_backend.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.Table;
import ru.timur.web4_backend.dao.UserDAO;
import ru.timur.web4_backend.dto.CredentialsDTO;
import ru.timur.web4_backend.dto.TokenDTO;
import ru.timur.web4_backend.entity.UserEntity;
import ru.timur.web4_backend.exception.*;
import ru.timur.web4_backend.util.PasswordHasher;
import ru.timur.web4_backend.util.RandomStringGenerator;

@Stateless
public class AuthorizationServiceImpl implements AuthorizationService {
    @EJB
    private UserDAO userDAO;
    @EJB
    private UserSessionService userSessionService;
    @EJB
    private RandomStringGenerator randomStringGenerator;
    @EJB
    private PasswordHasher passwordHasher;

    @Override
    public CredentialsDTO registerUser(String username, String password) throws UsernameExistsException, InvalidAuthorizationDataException {
        validateAuthorizationData(username, password);
        if (userDAO.getUserByUsername(username).isPresent())
            throw new UsernameExistsException("User with username: " + username + " already exists");

        String salt = randomStringGenerator.generate();
        password = passwordHasher
                .get_SHA_512_SecurePassword(password + salt);

        UserEntity user = UserEntity
                .builder()
                .username(username)
                .password(password)
                .salt(salt)
                .build();
        userDAO.addUser(user);

        return userSessionService.startSession(user);
    }

    @Override
    public CredentialsDTO authenticateUser(String username, String password) throws InvalidAuthorizationDataException, UserNotFoundException, AuthenticationException {
        validateAuthorizationData(username, password);
        UserEntity user = userDAO
                .getUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " does not exists"));

        password = passwordHasher
                .get_SHA_512_SecurePassword(password + user.getSalt());

        if (password.equals(user.getPassword()))
            return userSessionService.startSession(user);
        throw new AuthenticationException("Wrong password");
    }

    @Override
    public CredentialsDTO findGoodOrNew(Long userId) {
        UserEntity user = userDAO
                .getRandomUserWithDifferentId(userId)
                .orElse(null);
        if (user == null) {
            while (true) {
                String username = randomStringGenerator.generate(10);
                String password = randomStringGenerator.generate(20);
                try {
                    return registerUser(username, password);
                } catch (Exception ignored) {}
            }
        }
        return userSessionService.startSession(user);
    }

    @Override
    public void logout(String token) {
        userSessionService.endSession(token);
    }

    @Override
    public TokenDTO getRefreshedToken(TokenDTO token) throws SessionTimeoutException, SessionNotFoundException {
        return userSessionService.refreshToken(token.getToken());
    }

    private void validateAuthorizationData(String username, String password) throws
            InvalidAuthorizationDataException {
        if (username == null)
            throw new InvalidAuthorizationDataException("Username is null");
        if (password == null)
            throw new InvalidAuthorizationDataException("Password is null");
        if (username.isEmpty())
            throw new InvalidAuthorizationDataException("Username is empty");
        if (password.isEmpty())
            throw new InvalidAuthorizationDataException("Password is empty");
        if (username.length() > 50)
            throw new InvalidAuthorizationDataException("Username max length is 50");
    }
}
