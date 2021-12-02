import com.wrapper.spotify.SpotifyApi;
import spotifySimilar.SimilarArtists;
import spotifySimilar.SimilarTracks;

public class Main {

    private static SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken("BQCpz_FNZ2r-r8HI4LR6XUHcKgdPScCWxkxudQK9ZtWjBSGsoTebpyj7POBe_XmVedOCOq5VHtvpEBqQjPjFMTgGy6GGRVSc9rmPJdhOnFLCVB3ysuzfVkmTQTaojWxKkGiIRrfUaSiUjYWW3SyQWTAtO3NB59Y")
            .build();

    private final static String trackName = "Младший лейтенант";
    private final static String artistName = "Ирина Аллегрова";

    public static void main(String[] args) {
        System.out.println("Similar tracks for " + trackName);
        System.out.println();
        SimilarTracks similarTracks = new SimilarTracks(spotifyApi);
        similarTracks.findSimilarTracks(trackName);

        System.out.println();
        System.out.println("Similar artists for " + artistName);
        System.out.println();

        SimilarArtists similarArtists = new SimilarArtists(spotifyApi);
        similarArtists.findSimilarArtists(artistName);
    }

}
