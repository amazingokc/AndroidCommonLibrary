package com.runde.commonlibrary.bean;

import java.io.Serializable;

/**
 * 作者：xiaoguoqing
 * 创建时间：2019-01-11 下午 7:04
 * 文件描述：单个消息体(推送消息)
 */

public class  SingleMsgBean implements Serializable {

    private String targetType;              //消息类型（文本、图片、图文、直播、课程..)
    private String title;                   //标题
    private String content;                 //内容
    private boolean needRead;               //是否未读
    private String imageUrl;                //图片地址
    private long id;                        //消息id
    private String businessType;            //业务类型（活动精选、我的资产、系统通知、客服）
    private String businessTypeName;        //业务类型（中文名）
    private String target;                  //目标地址
    private String pushTime;                //时间

    private String indexPullType;               //once只看一次，everytimes每次都看private String indexPullType;
    private Button button;                      //
    private boolean hasButton;                  //
    private String menuType;
    private String pushModel;
    private String hasNotdisturb;
    private String createAt;
    private String updateAt;
    private String templateCode;
    private String templateName;
    private String effectiveDate;
    private boolean deleted;
    private boolean open;
    private long sortValue;
    private long serialVersionUID;
    private long priorityLevel;
    private String instanceCode;
    private long delayMinute;
    private long templateId;

    private SingleMsgParaBean parameters;

    //是否是推送信息
    private String isPush;

    public String getIsPush() {
        return isPush;
    }

    public void setIsPush(String isPush) {
        this.isPush = isPush;
    }



    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }

    public String getPushModel() {
        return pushModel;
    }

    public void setPushModel(String pushModel) {
        this.pushModel = pushModel;
    }

    public String getHasNotdisturb() {
        return hasNotdisturb;
    }

    public void setHasNotdisturb(String hasNotdisturb) {
        this.hasNotdisturb = hasNotdisturb;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public long getSortValue() {
        return sortValue;
    }

    public void setSortValue(long sortValue) {
        this.sortValue = sortValue;
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSerialVersionUID(long serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
    }

    public long getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(long priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public String getInstanceCode() {
        return instanceCode;
    }

    public void setInstanceCode(String instanceCode) {
        this.instanceCode = instanceCode;
    }

    public long getDelayMinute() {
        return delayMinute;
    }

    public void setDelayMinute(long delayMinute) {
        this.delayMinute = delayMinute;
    }

    public long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(long templateId) {
        this.templateId = templateId;
    }

    public class Button implements Serializable {

        private String confirm;
        private String cancel;

        public String getConfirm() {
            return confirm;
        }

        public void setConfirm(String confirm) {
            this.confirm = confirm;
        }

        public String getCancel() {
            return cancel;
        }

        public void setCancel(String cancel) {
            this.cancel = cancel;
        }

        @Override
        public String toString() {
            return "Button{" +
                    "confirm='" + confirm + '\'' +
                    ", cancel='" + cancel + '\'' +
                    '}';
        }
    }

    public boolean isHasButton() {
        return hasButton;
    }

    public void setHasButton(boolean hasButton) {
        this.hasButton = hasButton;
    }

    public String getIndexPullType() {
        return indexPullType;
    }

    public void setIndexPullType(String indexPullType) {
        this.indexPullType = indexPullType;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isNeedRead() {
        return needRead;
    }

    public void setNeedRead(boolean needRead) {
        this.needRead = needRead;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public SingleMsgParaBean getParameters() {
        return parameters;
    }

    public void setParameters(SingleMsgParaBean parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "SingleMsgBean{" +
                "targetType='" + targetType + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", needRead=" + needRead +
                ", imageUrl='" + imageUrl + '\'' +
                ", id=" + id +
                ", businessType='" + businessType + '\'' +
                ", target='" + target + '\'' +
                ", pushTime='" + pushTime + '\'' +
                ", indexPullType='" + indexPullType + '\'' +
                ", button=" + button +
                ", hasButton=" + hasButton +
                ", menuType='" + menuType + '\'' +
                ", pushModel='" + pushModel + '\'' +
                ", hasNotdisturb='" + hasNotdisturb + '\'' +
                ", createAt='" + createAt + '\'' +
                ", updateAt='" + updateAt + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", templateName='" + templateName + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", deleted=" + deleted +
                ", open=" + open +
                ", sortValue=" + sortValue +
                ", serialVersionUID=" + serialVersionUID +
                ", priorityLevel=" + priorityLevel +
                ", instanceCode='" + instanceCode + '\'' +
                ", delayMinute=" + delayMinute +
                ", templateId=" + templateId +
                ", parameters=" + parameters +
                ", isPush='" + isPush + '\'' +
                '}';
    }
}
