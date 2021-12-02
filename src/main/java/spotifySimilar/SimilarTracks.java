package spotifySimilar;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.special.SearchResult;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Recommendations;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.List;

@Getter
public class SimilarTracks {

    private final SpotifyApi spotifyApi;
    private StringBuilder responseMessage;

    @SneakyThrows
    public SimilarTracks(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
        responseMessage = new StringBuilder();
    }

    public List<List<TrackSimplified>> findSimilarTracks(String trackName) {

        Track track = findTrackByName(trackName);
        assert track != null;
        responseMessage.append("Трек: ");
        writeAboutTrack(track);
        responseMessage.append("\n\nСписок похожих песен: \n");

        Recommendations recommendations = getRecommendations(track.getId());
        assert recommendations != null;
        for (TrackSimplified recommendTrack : recommendations.getTracks()) {
            writeAboutTrack(recommendTrack);
        }
        return Arrays.asList(Arrays.asList(recommendations.getTracks()));
    }

    public Track findTrackByName(String trackName) {
        try {
            SearchResult searchItemRequest = spotifyApi.searchItem(trackName, "track").build().execute();
            return searchItemRequest.getTracks().getItems()[0];
        } catch (Exception e) {
            System.out.println("Something went wrong!\n" + e.getMessage());
            System.out.println("Can't find track!");
            return null;
        }
    }

    public Recommendations getRecommendations(String trackId) {
        try {
            return spotifyApi.getRecommendations().seed_tracks(trackId).limit(20).build().execute();
        } catch (Exception e) {
            System.out.println("Something went wrong!\n" + e.getMessage());
            System.out.println("Can't get recommendations!");
            return null;
        }
    }

    public void writeAboutTrack(Track track) {
        responseMessage.append(track.getName()).append(" - ").append(getStringArtists(track.getArtists())).append("\n");
        System.out.print(track.getName() + " - ");
        System.out.println(getStringArtists(track.getArtists()));
    }

    public void writeAboutTrack(TrackSimplified track) {
        responseMessage.append(track.getName()).append(" - ").append(getStringArtists(track.getArtists())).append("\n");
        System.out.print(track.getName() + " - ");
        System.out.println(getStringArtists(track.getArtists()));
    }

    public String getStringArtists(ArtistSimplified[] artists) {
        StringBuilder stringBuilder = new StringBuilder();
        String breakString = ", ";
        for (ArtistSimplified artist : artists) {
            stringBuilder.append(artist.getName()).append(breakString);
        }
        stringBuilder.setLength(stringBuilder.length() - breakString.length());
        return stringBuilder.toString();
    }
}
