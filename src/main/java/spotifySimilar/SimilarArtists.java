package spotifySimilar;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.special.SearchResult;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.requests.data.artists.GetArtistsRelatedArtistsRequest;

import java.util.Arrays;
import java.util.List;

public class SimilarArtists {

    private final SpotifyApi spotifyApi;

    public SimilarArtists(SpotifyApi spotifyApi) {
        this.spotifyApi = spotifyApi;
    }

    public List<Artist> findSimilarArtists(String artistName) {
        Artist artist = findArtistByName(artistName);

        assert artist != null;
        System.out.print("Your artist is ");
        writeAboutArtist(artist);

        return getSimilarArtists(artist.getId());

    }

    private Artist findArtistByName(String artistName) {
        try {
            SearchResult searchItemRequest = spotifyApi.searchItem(artistName, "artist").build().execute();
            return searchItemRequest.getArtists().getItems()[0];
        } catch (Exception e) {
            System.out.println("Something went wrong!\n" + e.getMessage());
            System.out.println("Can't find artist!");
            return null;
        }
    }

    private List<Artist> getSimilarArtists(String artistId) {
        try {
            List<Artist> artists =  Arrays.asList(new GetArtistsRelatedArtistsRequest.Builder(spotifyApi.getAccessToken())
                    .setDefaults(
                            spotifyApi.getHttpManager(),
                            spotifyApi.getScheme(),
                            spotifyApi.getHost(),
                            spotifyApi.getPort())
                    .id(artistId)
                    .build().execute());

            artists.forEach(this::writeAboutArtist);

            return artists;
        } catch (Exception e) {
            System.out.println("Something went wrong!\n" + e.getMessage());
            System.out.println("Can't get recommendations!");
            return null;
        }
    }

    private void writeAboutArtist(Artist artist) {
        System.out.println(artist.getName());
    }

}
