package gongren.com.dlg.javabean;

import java.util.List;

/**
 * Created by lenovo on 2017/5/23.
 */

public class ShareBean {

    /**
     * code : 0
     * msg : 成功
     * total : 0
     * totalpage : 0
     * data : [{"taskId":"27587457774904861149432932794","taskTitle":"【活动/36】  36/天，点开接单赚钱吧!","taskDescription":"恶魔巫师","detailsUrl":"http://10.20.31.201:8080/api/appHtmlRestApi/taskDetail?taskId=27579244721163062602337469007","xCoordinate":121.384097,"yCoordinate":31.233217,"userLogo":null}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * taskId : 27587457774904861149432932794
         * taskTitle : 【活动/36】  36/天，点开接单赚钱吧!
         * taskDescription : 恶魔巫师
         * detailsUrl : http://10.20.31.201:8080/api/appHtmlRestApi/taskDetail?taskId=27579244721163062602337469007
         * xCoordinate : 121.384097
         * yCoordinate : 31.233217
         * userLogo : null
         */

        private String taskId;
        private String taskTitle;
        private String taskDescription;
        private String detailsUrl;
        private double xCoordinate;
        private double yCoordinate;
        private String userLogo;

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTaskTitle() {
            return taskTitle;
        }

        public void setTaskTitle(String taskTitle) {
            this.taskTitle = taskTitle;
        }

        public String getTaskDescription() {
            return taskDescription;
        }

        public void setTaskDescription(String taskDescription) {
            this.taskDescription = taskDescription;
        }

        public String getDetailsUrl() {
            return detailsUrl;
        }

        public void setDetailsUrl(String detailsUrl) {
            this.detailsUrl = detailsUrl;
        }

        public double getXCoordinate() {
            return xCoordinate;
        }

        public void setXCoordinate(double xCoordinate) {
            this.xCoordinate = xCoordinate;
        }

        public double getYCoordinate() {
            return yCoordinate;
        }

        public void setYCoordinate(double yCoordinate) {
            this.yCoordinate = yCoordinate;
        }

        public String getUserLogo() {
            return userLogo;
        }

        public void setUserLogo(String userLogo) {
            this.userLogo = userLogo;
        }
    }
}
