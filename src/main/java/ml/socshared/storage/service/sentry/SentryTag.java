package ml.socshared.storage.service.sentry;

public enum SentryTag {
    SAVE_GROUP("type", "save_group"),
    DELETE_GROUP_BY_ID("type", "delete_group_by_id"),
    DELETE_GROUP_BY_VK_ID("type", "delete_group_by_vk_id"),
    DELETE_GROUP_BY_FB_ID("type", "delete_group_by_fb_id"),
    GET_GROUP_BY_USER_ID("type", "get_group_by_user_id"),
    GET_GROUP_BY_USER_ID_AND_SOCIAL_NETWORK("type", "get_group_by_user_id_and_social_network"),
    GET_BY_USER_ID_AND_VK_ID("type", "get_by_user_id_and_vk_id"),
    GET_BY_USER_ID_AND_FACEBOOK_ID("type", "get_by_user_id_and_facebook_id"),
    DELETE_VK_GROUPS_BY_USER_ID("type", "delete_vk_groups_by_user_id"),
    DELETE_FACEBOOK_GROUPS_BY_USER_ID("type", "delete_facebook_groups_by_user_id"),
    SAVE_PUBLICATION("type", "save_publication"),
    GET_NOT_PUBLISHING_PUBLICATION("type", "get_not_publishing_publication"),
    GET_PUBLISHING_AFTER_PUBLICATION("type", "get_publishing_after_publication"),
    GET_PUBLICATION_BY_GROUP_ID("type", "get_publication_by_group_id");

    SentryTag(String t, String tag) {
        type = t;
        sentryTag = tag;
    }

    public String type() {
        return type;
    }
    public String value() {
        return sentryTag;
    }

    private final String sentryTag;
    private final String type;
    public static final String SERVICE_NAME = "STS";

}
