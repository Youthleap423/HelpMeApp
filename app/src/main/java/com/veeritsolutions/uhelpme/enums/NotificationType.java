package com.veeritsolutions.uhelpme.enums;

/**
 * Created by ${Hitesh} on 18-Apr-17.
 */

public enum NotificationType {

    BirthDay(1),
    AppointmentRequest(2),
    AppointmentApprove(3),
    AppointmentReject(4),
    AppointmentClientConfirm(5),
    AppointmentVetConfirm(6),
    AppointmentClientCancel(7),
    AppointmentVetCancel(8),
    AppointmentClientBoth(9),
    AppointmentVetBoth(10);

    int notificationType;

    NotificationType(int i) {
        this.notificationType = i;
    }

    public int getType() {
        return notificationType;
    }

    public void setRegisterBy(int notificationType) {
        this.notificationType = notificationType;
    }
}
