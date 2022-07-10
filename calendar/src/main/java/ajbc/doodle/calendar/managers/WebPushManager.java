package ajbc.doodle.calendar.managers;

import ajbc.doodle.calendar.Application;
import ajbc.doodle.calendar.ServerKeys;
import ajbc.doodle.calendar.entities.webpush.Subscription;
import ajbc.doodle.calendar.services.CryptoService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;

/**
 * A class that hold the utilities for push notifications
 */
@Getter
@Component
public class WebPushManager {

	private final ServerKeys serverKeys;
	private final CryptoService cryptoService;
	private final HttpClient httpClient;
	private final Algorithm jwtAlgorithm;
	private final ObjectMapper objectMapper;

	public WebPushManager(ServerKeys serverKeys, CryptoService cryptoService, ObjectMapper objectMapper) {
		this.serverKeys = serverKeys;
		this.cryptoService = cryptoService;
		this.httpClient = HttpClient.newHttpClient();
		this.objectMapper = objectMapper;

		this.jwtAlgorithm = Algorithm.ECDSA256(this.serverKeys.getPublicKey(), this.serverKeys.getPrivateKey());
	}

//    /**
//     * @return true if the subscription is no longer valid and can be removed, false
//     * if everything is okay
//     */
//    private boolean sendPushMessage(Subscription subscription, byte[] body) {
//        String origin = null;
//        try {
//            URL url = new URL(subscription.getEndpoint());
//            origin = url.getProtocol() + "://" + url.getHost();
//        } catch (MalformedURLException e) {
//            Application.logger.error("create origin", e);
//            return true;
//        }
//
//        Date today = new Date();
//        Date expires = new Date(today.getTime() + 12 * 60 * 60 * 1000);
//
//        String token = JWT.create().withAudience(origin).withExpiresAt(expires)
//                .withSubject("mailto:example@example.com").sign(this.jwtAlgorithm);
//
//        URI endpointURI = URI.create(subscription.getEndpoint());
//
//        HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder();
//        if (body != null) {
//            httpRequestBuilder.POST(HttpRequest.BodyPublishers.ofByteArray(body)).header("Content-Type", "application/octet-stream")
//                    .header("Content-Encoding", "aes128gcm");
//        } else {
//            httpRequestBuilder.POST(HttpRequest.BodyPublishers.ofString(""));
//            // httpRequestBuilder.header("Content-Length", "0");
//        }
//
//        HttpRequest request = httpRequestBuilder.uri(endpointURI).header("TTL", "180")
//                .header("Authorization", "vapid t=" + token + ", k=" + this.serverKeys.getPublicKeyBase64()).build();
//        try {
//            HttpResponse<Void> response = this.httpClient.send(request, HttpResponse.BodyHandlers.discarding());
//
//            switch (response.statusCode()) {
//                case 201:
//                    Application.logger.info("Push message successfully sent: {}", subscription.getEndpoint());
//                    break;
//                case 404:
//                case 410:
//                    Application.logger.warn("Subscription not found or gone: {}", subscription.getEndpoint());
//                    // remove subscription from our collection of subscriptions
//                    return true;
//                case 429:
//                    Application.logger.error("Too many requests: {}", request);
//                    break;
//                case 400:
//                    Application.logger.error("Invalid request: {}", request);
//                    break;
//                case 413:
//                    Application.logger.error("Payload size too large: {}", request);
//                    break;
//                default:
//                    Application.logger.error("Unhandled status code: {} / {}", response.statusCode(), request);
//            }
//        } catch (IOException | InterruptedException e) {
//            Application.logger.error("send push message", e);
//        }
//
//        return false;
//    }

}
