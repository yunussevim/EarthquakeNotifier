package com.huawei.earthquakenotifier.model;

import com.google.gson.annotations.SerializedName;

public class NotificationBody {

    @SerializedName("validate_only")
    private boolean validation;
    @SerializedName("message")
    private Builder.Message message;

    public NotificationBody(boolean validation, Builder.Message message){
        this.validation = validation;
        this.message = message;
    }

    public static class Builder {
        private String notificationTitle;
        private String notificationBody;
        private String[] notificationPushToken;
        public Builder(String notificationTitle, String notificationBody, String pushToken) {
            this.notificationTitle = notificationTitle;
            this.notificationBody = notificationBody;
            this.notificationPushToken = new String[1];
            this.notificationPushToken[0] = pushToken;
        }
        public NotificationBody build() {
            ClickAction clickAction = new ClickAction(3);
            AndroidNotification androidNotification = new AndroidNotification(notificationTitle, notificationBody, clickAction);
            AndroidConfig androidConfig = new AndroidConfig(androidNotification);
            Notification notification = new Notification(notificationTitle, notificationBody);
            Message message = new Message(notification, androidConfig, notificationPushToken);
            NotificationBody notificationMessage =
                    new NotificationBody(false, message);
            return notificationMessage;
        }
        public static class Message {
            @SerializedName("notification")
            private Notification notification;
            @SerializedName("android")
            private AndroidConfig android;
            @SerializedName("token")
            private String[] arrayToken;
            public Message(Notification notification, AndroidConfig android, String[] arrayToken) {
                this.notification = notification;
                this.android = android;
                this.arrayToken = arrayToken;
            }
        }
        public static class Notification {
            @SerializedName("title")
            private String title;
            @SerializedName("body")
            private String body;
            public Notification(String title, String body) {
                this.title = title;
                this.body = body;
            }
        }
        class AndroidConfig {
            @SerializedName("notification")
            AndroidNotification notification;
            public AndroidConfig(AndroidNotification notification) {
                this.notification = notification;
            }
        }
        class AndroidNotification {
            @SerializedName("title")
            String title;
            @SerializedName("body")
            String body;
            @SerializedName("click_action")
            ClickAction clickaction;
            public AndroidNotification(String title, String body, ClickAction clickAction) {
                this.title = title;
                this.body = body;
                this.clickaction = clickAction;
            }
        }
        public static class ClickAction {
            @SerializedName("type")
            private int type;
            @SerializedName("intent")
            private String intent;
            public ClickAction(int type) {
                this.type = type;
            }
            public ClickAction(int type, String intent) {
                this.type = type;
                this.intent = intent;
            }
        }
    }
}
