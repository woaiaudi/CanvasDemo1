package com.zczy.canvasdemo1.pickPhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 16/4/11.
 */
public class IMGIDS {
    private static IMGIDS ourInstance = new IMGIDS();

    public static IMGIDS getInstance() {
        return ourInstance;
    }

    /**
     * 记录 requestCode于 imageviewID的一一对应关系,不会重复
     * 如: [1233323,123333,...]
     */
    private List<String> IDS;

    private IMGIDS() {
        IDS = new ArrayList<String>();
        //填充一写数据,避免 下表从0开始
        IDS.add("A1");
        IDS.add("A2");
        IDS.add("A3");
        IDS.add("A4");
    }

    //注册 一个 imageviewID
    public void registIMGID(int imgId){
        if (!IDS.contains(imgId+"")){
            IDS.add(imgId+"");
        }
    }

    /**
     * 查询一个注册的ID
     * @param imgId
     * @return
     */
    public int queryRequestCode(int imgId){
        int returnRCODE = -1;
        for (int i = 0;i<IDS.size();i++){
            String tmpIds = IDS.get(i);
            if (tmpIds.equals(imgId+"")){
                returnRCODE = i;
                break;
            }
        }

        if (returnRCODE == -1){
            //没有时,将这个id添加进去,然后返回 requestcode
            IDS.add(imgId+"");
            returnRCODE = IDS.size()-1;
        }

        return returnRCODE;
    }

    public int queryIMGID(int reqCode){
        int returnIMGID = -1;
        if (reqCode<IDS.size()){
            try {
                returnIMGID = Integer.parseInt(IDS.get(reqCode));
            }catch (Exception e){
                e.printStackTrace();
                returnIMGID = -1;
            }
        }
        return returnIMGID;
    }

}
