package Program.Server;

import Program.Common.DataClasses.Worker;

import java.util.LinkedList;

class InnerServerTransporter {
    LinkedList<Worker> WorkersData = null;
    String args;
    String errorMsg = null;

    public LinkedList<Worker> getWorkersData() {
        return WorkersData;
    }

    public void setWorkersData(LinkedList<Worker> workersData) {
        WorkersData = workersData;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
