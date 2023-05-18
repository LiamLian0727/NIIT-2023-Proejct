package model;

public class Result {
    private String count = "0";
    private String time = "0";
    private String size = "0";
    private String retry = "0";

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getRetry() {
        return retry;
    }

    public void setRetry(String retry) {
        this.retry = retry;
    }

    @Override
    public String toString() {
        return "Result{" +
                "count='" + count + '\'' +
                ", time='" + time + '\'' +
                ", size='" + size + '\'' +
                ", retry='" + retry + '\'' +
                '}';
    }
}
