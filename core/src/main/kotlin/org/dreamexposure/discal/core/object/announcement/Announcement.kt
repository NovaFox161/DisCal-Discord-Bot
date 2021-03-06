package org.dreamexposure.discal.core.`object`.announcement

import discord4j.common.util.Snowflake
import discord4j.core.`object`.entity.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.dreamexposure.discal.core.enums.announcement.AnnouncementModifier
import org.dreamexposure.discal.core.enums.announcement.AnnouncementType
import org.dreamexposure.discal.core.enums.event.EventColor
import org.dreamexposure.discal.core.serializers.SnowflakeAsStringSerializer
import org.dreamexposure.discal.core.serializers.UUIDasStringSerializer
import java.util.*

@Serializable
data class Announcement(
        @Serializable(with = SnowflakeAsStringSerializer::class)
        @SerialName("guild_id")
        val guildId: Snowflake,
) {
    @Serializable(with = UUIDasStringSerializer::class)
    @SerialName("id")
    var announcementId: UUID = UUID.randomUUID()
        private set

    @SerialName("subscriber_roles")
    val subscriberRoleIds: MutableList<String> = mutableListOf()

    @SerialName("subscriber_users")
    val subscriberUserIds: MutableList<String> = arrayListOf()

    @SerialName("channel_id")
    var announcementChannelId: String = "N/a"
    var type = AnnouncementType.UNIVERSAL
    var modifier = AnnouncementModifier.BEFORE

    @SerialName("event_id")
    var eventId: String = "N/a"

    @SerialName("event_color")
    var eventColor = EventColor.NONE

    @SerialName("hours")
    var hoursBefore = 0

    @SerialName("minutes")
    var minutesBefore = 0
    var info = "None"

    var enabled = true

    @SerialName("info_only")
    var infoOnly = false
    var publish = false

    //Stuff for wizards
    @Transient
    var creatorMessage: Message? = null

    @Transient
    var editing = false

    @Transient
    var lastEdit = System.currentTimeMillis()

    constructor(guildId: Snowflake, announcementId: UUID) : this(guildId) {
        this.announcementId = announcementId
    }

    companion object {
        fun copy(from: Announcement, copyId: Boolean = false): Announcement {
            val to = from.copy()
            if (copyId)
                to.announcementId = UUID.randomUUID()

            //Copy all the other params...
            to.subscriberRoleIds.addAll(from.subscriberRoleIds)
            to.subscriberUserIds.addAll(from.subscriberUserIds)
            to.announcementChannelId = from.announcementChannelId
            to.type = from.type
            to.modifier = from.modifier
            to.eventId = from.eventId
            to.eventColor = from.eventColor
            to.hoursBefore = from.hoursBefore
            to.minutesBefore = from.minutesBefore
            to.info = from.info
            to.enabled = from.enabled
            to.infoOnly = from.infoOnly
            to.publish = to.publish

            return to
        }
    }

    fun getSubscriberRoleIdString(): String {
        val subs = StringBuilder()

        for ((i, sub) in this.subscriberRoleIds.withIndex()) {
            if (sub.isNotBlank()) {
                if (i == 0) subs.append(sub)
                else subs.append(",").append(sub)
            }
        }

        return subs.toString()
    }

    fun getSubscriberUserIdString(): String {
        val subs = StringBuilder()
        for ((i, sub) in this.subscriberUserIds.withIndex()) {
            if (sub.isNotBlank()) {
                if (i == 0) subs.append(sub)
                else subs.append(",").append(sub)
            }
        }

        return subs.toString()
    }

    fun setSubscriberRoleIdsFromString(subList: String) {
        this.subscriberRoleIds += subList.split(",").filter(String::isNotBlank)
    }

    fun setSubscriberUserIdsFromString(subList: String) {
        this.subscriberUserIds += subList.split(",").filter(String::isNotBlank)
    }

    fun hasRequiredValues(): Boolean {
        return (this.minutesBefore != 0 || this.hoursBefore != 0)
                && !(this.type == AnnouncementType.SPECIFIC && this.eventId == "N/a")
                && this.announcementChannelId != "N/a"
    }
}
