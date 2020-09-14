package neueda.team1.portfolio_manager.entity;

import java.util.List;

public class SecurityHistoryResult {
    private int timeStamp;
    private List<SecurityHistory> data;
    private String status;

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<SecurityHistory> getData() {
        return data;
    }

    public void setData(List<SecurityHistory> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
