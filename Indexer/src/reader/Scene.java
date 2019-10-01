package reader;

public class Scene extends Document {

    private String playId, sceneId;
    @SuppressWarnings("unused")
    private int sceneNum;

    public Scene(String pId, String sId, int sNum, String text) {
        // scene-number is going to be our document id for the shakespeare database
        // "playId#sceneId" will be the scene's actual id.
        super(sNum, pId + "#" + sId, text); // term vector created
        playId = pId;
        sceneId = sId;
        sceneNum = sNum;
    }

    public String getSceneId() {
        return playId + "#" + sceneId;
    }
}
