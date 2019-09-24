package reader;

public class Scene extends Document {

    private String playId, sceneId;
    private int sceneNum;

    public Scene(String pId, String sId, int sNum, String text) {
        // playId + scneId is going to be our document id
        super(pId + sId, text); // term vector created
        playId = pId;
        sceneId = sId;
        sceneNum = sNum;
    }
}
