package com.nor2code.springboot.thymeleafdemo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;

@Service // szolgáltatás (service) komponens, és az alkalmazás indulásakor Spring bean-ként regisztrálódik
public class ExchangeRateServiceImpl implements ExchangeRateService{

    // A RestTemplate egyszerűbb és elterjedtebb a szinkron HTTP hívásokhoz, egyszerű, szinkron HTTP kliens osztálya, Ezzel fog majd REST API-t hívni.
    private final RestTemplate restTemplate = new RestTemplate();

/*    @Override
    public BigDecimal getHufToEurRate() { // A visszatérési érték típusa BigDecimal, amely pontos számításokra alkalmas (pl. pénzügyi adatoknál).
       String url = "https://api.frankfurter.app/latest?from=HUF&to=EUR"; // Meghatároz egy URL-t, amely egy nyilvános árfolyam API végpont.

        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class); // HTTP GET kérést küld az előzőleg megadott URL-re. A válaszból egy Map-et vár vissza, amely JSON-ként jön az API-ból, és kulcs-érték párokra konvertálódik.
            System.out.println("API response: " + response);

            if (response != null && response.containsKey("rates")) {
                Map<String, Double> rates = (Map<String, Double>) response.get("rates");
                Double eurRate = rates.get("EUR");
                return eurRate != null ? BigDecimal.valueOf(eurRate) : BigDecimal.ZERO; // Ha sikerült lekérni az árfolyamot, BigDecimal típusban visszaadja. Ha null, akkor visszatér BigDecimal.ZERO-val.
            }
        }
        // 4xx hibák (pl. 404 Not Found)
        catch (HttpClientErrorException e) {
            System.err.println("HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
        catch (RestClientException e) {
            // Általános hálózati vagy REST hiba
            System.err.println("REST error: " + e.getMessage());
        }
        catch (Exception e) {
            // Bármi más hiba (pl. castolás, nullpointer, stb.)
            System.err.println("Unexpected error: " + e.getMessage());
        }


        return BigDecimal.ZERO;
    } */

    // WebClient
    private final WebClient webClient = WebClient.create("https://api.frankfurter.app");

    @Override
    public BigDecimal getHufToEurRate() {
        try {
            Map response = webClient.get() // HTTP GET kérést készít elő.
                    .uri("/latest?from=HUF&to=EUR") // Az API végpont URI-ja (a base URL-t a webClient példány korábban kapta meg konfigurációban).
                    .retrieve() // Végrehajtja a kérést, és várja a választ.
                    .bodyToMono(Map.class) // A választ egy aszinkron (Mono) objektumként (Map-ként) értelmezi (feltételezve, hogy JSON válasz).
                    .block(); // Megvárja a válasz beérkezését (blokkolja a végrehajtást), és visszaadja a tényleges Map objektumot.

            System.out.println("API response: " + response);

            if (response != null && response.containsKey("rates")) {
                Map<String, Double> rates = (Map<String, Double>) response.get("rates");
                Double eurRate = rates.get("EUR");
                return eurRate != null ? BigDecimal.valueOf(eurRate) : BigDecimal.ZERO;
            }
        }
        // 4xx hibák (pl. 404 Not Found)
        catch (HttpClientErrorException e) {
            System.err.println("HTTP Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }
        catch (RestClientException e) {
            // Általános hálózati vagy REST hiba
            System.err.println("REST error: " + e.getMessage());
        }
        catch (Exception e) {
            // Bármi más hiba (pl. castolás, nullpointer, stb.)
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return BigDecimal.ZERO;
    }
}
