package neueda.team1.portfolio_manager.entity;

import java.util.List;

public class SecurityResult {
    private int timeStamp;
    private List<Security> data;
    private String status;

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<Security> getData() {
        return data;
    }

    public void setData(List<Security> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

