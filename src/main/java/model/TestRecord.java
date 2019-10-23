package model;

public class TestRecord {
    private String name;
    private String numberOfRules;
    private String nrOfRules;
    private String useCanonicalModel;
    private String rulesProviderId;
    private String matchRatio;
    private String score;
    private int hashCode;

    public TestRecord(String name, String numberOfRules, String nrOfRules, String useCanonicalModel, String rulesProviderId, String matchRatio, String score) {
        this.name = name;
        this.numberOfRules = numberOfRules;
        this.nrOfRules = nrOfRules;
        this.useCanonicalModel = useCanonicalModel;
        this.rulesProviderId = rulesProviderId;
        this.matchRatio = matchRatio;
        this.score = score;

        this.setHashCode();
    }

    public TestRecord() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberOfRules() {
        return numberOfRules;
    }

    public void setNumberOfRules(String numberOfRules) {
        this.numberOfRules = numberOfRules;
    }

    public String getUseCanonicalModel() {
        return useCanonicalModel;
    }

    public void setUseCanonicalModel(String useCanonicalModel) {
        this.useCanonicalModel = useCanonicalModel;
    }

    public String getRulesProviderId() {
        return rulesProviderId;
    }

    public void setRulesProviderId(String rulesProviderId) {
        this.rulesProviderId = rulesProviderId;
    }

    public String getMatchRatio() {
        return matchRatio;
    }

    public void setMatchRatio(String matchRatio) {
        this.matchRatio = matchRatio;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "TestResults{" +
                "hashCode=" + hashCode + '\'' +
                "name='" + name + '\'' +
                ", numberOfRules='" + numberOfRules + '\'' +
                ", nrOfRules='" + nrOfRules + '\'' +
                ", useCanonicalModel=" + useCanonicalModel +
                ", rulesProviderId='" + rulesProviderId + '\'' +
                ", matchRatio='" + matchRatio + '\'' +
                ", score=" + score +
                '}';
    }

    public int getHashCode() {
        return hashCode;
    }

    public int getHashCode(String name, String numberOfRules, String nrOfRules, String useCanonicalModel, String rulesProviderId, String matchRatio) {
        int hashCode = 0;

        if (name != null ) hashCode = 31 * name.hashCode();
        if (numberOfRules != null ) hashCode = 31 * hashCode + numberOfRules.hashCode();
        if (nrOfRules != null ) hashCode = 31 * hashCode + nrOfRules.hashCode();
        if (useCanonicalModel != null ) hashCode = 31 * hashCode + useCanonicalModel.hashCode();
        if (rulesProviderId != null ) hashCode = 31 * hashCode + rulesProviderId.hashCode();
        if (matchRatio != null ) hashCode = 31 * hashCode + matchRatio.hashCode();

        return hashCode;
    }

    public void setHashCode() {
        int hashCode = 0;

        if (this.name != null ) hashCode = 31 * this.name.hashCode();
        if (this.numberOfRules != null ) hashCode = 31 * hashCode + this.numberOfRules.hashCode();
        if (this.nrOfRules != null ) hashCode = 31 * hashCode + this.nrOfRules.hashCode();
        if (this.useCanonicalModel != null ) hashCode = 31 * hashCode + this.useCanonicalModel.hashCode();
        if (this.rulesProviderId != null ) hashCode = 31 * hashCode + this.rulesProviderId.hashCode();
        if (this.matchRatio != null ) hashCode = 31 * hashCode + this.matchRatio.hashCode();

        this.hashCode = hashCode;
    }

    public String getNrOfRules() {
        return nrOfRules;
    }

    public void setNrOfRules(String nrOfRules) {
        this.nrOfRules = nrOfRules;
    }
}
