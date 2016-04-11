package com.zczy.canvasdemo1;


import java.io.Serializable;

public class UpLoadModel  implements Serializable{
    private String id;
    private String fileId;

    private String filePath;

    private String msg;

    private String success;
    private String index;
    public UpLoadModel() {
        this.fileId = "0";
        this.filePath = "";
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFileId() {
        return fileId;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getSuccess() {
        return success;
    }
    public void setSuccess(String success) {
        this.success = success;
    }
    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * 接口 传过来的对象中，可能是id，可能是fileId，这里统一 都有值
     */
    public void unitTheIdSame(){
        if (null != this.getId()
                &&this.getId().length()>0) {
            this.setFileId(this.getId());
        }else if (null != this.getFileId()
                &&this.getFileId().length()>0) {
            this.setId(this.getFileId());
        }
    }
}
