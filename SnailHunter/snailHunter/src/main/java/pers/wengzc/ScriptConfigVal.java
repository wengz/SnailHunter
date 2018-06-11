package pers.wengzc;

public class ScriptConfigVal {

    private long timeCriterion = 300;

    private boolean huntInMainThread = true;

    public long getTimeCriterion() {
        return timeCriterion;
    }

    public void setTimeCriterion(long timeCriterion) {
        this.timeCriterion = timeCriterion;
    }

    public boolean isHuntInMainThread() {
        return huntInMainThread;
    }

    public void setHuntInMainThread(boolean huntInMainThread) {
        this.huntInMainThread = huntInMainThread;
    }
}
