package reader;

public class Scene extends Document {

    private String playId, sceneId;
    private int sceneNum;

    public Scene(String pId, String sId, int sNum, String text) {
        super(text); // term vector created
        playId = pId;
        sceneId = sId;
        sceneNum = sNum;
    }
}
