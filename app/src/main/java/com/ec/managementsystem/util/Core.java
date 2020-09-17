package com.ec.managementsystem.util;

import android.os.Environment;

public class Core {
    public static final int REQUEST_CAMERA_ACTIVITY = 303;
    public static final int REQUEST_SIGNATURE_ACTIVITY = 302;
    public static final int REQUEST_PDF_VIEW = 304;
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final int ASPHALT_MAX_DISTANCE = 150000; //metros
    public static final String DATE_FORMAT_TZ = "yyyy-MM-dd hh:mm:ss a (z)";
    public static final String TIME_FORMAT_TZ = "hh:mm:ss a (z)";
    public static final String keyEncoderUrl = "o5g4lpJjdGumLr8Ne58A43VibV0=";
    public static final String geoCodeUrl = "http://rg.geo.cx:8080/proxyrg/rg?client=assured-741&gp=%lat,%lon&mv=0&d=1234567890";
    public static final String geoCodeUrl2 = "http://rg.geo.cx:8080/proxyrg2/rg?client=assured-741&gp=%lat,%lon&mv=0&d=1234567890";
    public static final long Sep292020Timestamp = 1601337600;// 1601337600 GMT Tuesday, September 29, 2020 12:00:00 AM;
    public static final int TIME_OUT_WEB_SERVICES = 120000; //Milisegundos 2 min

    public enum BTDeviceType {
        UNKNOWN, ATBS, VNA, STARTLINK, GNX, AX, PT, LMU, LAIRD, SYRUS, IOSIX, TELTONIKA, AK, KT, BLUE_LINK, GEOMETRIS, ST20, VEOSPEHRE, LX, LMU3640, AX11, ELD4U, VT7, LMUSerial, PT40  /*, BT, UC*/
    }

    public enum HeaderType {
        UNKNOWN, EVENT, SHIPMENT
    }

    public enum ShipmentAction {
        INSERT, UPDATE, DELETE
    }

    public enum EventType {
        UNKNOWN, DUTY_STATUS, INTERMEDIATE_LOG, ALLOWED_CONDITIONS, CERTIFICATION, LOG_IN_OUT, ENGINE_ON_OFF, DIAGNOSTIC
    }

    public enum EventTypeAction {
        UNKNOWN("UNKNOWN", 100),
        ADD_DUTY_STATUS("ADD_DUTY_STATUS", 101),
        ADD_ALLOWED_CONDITIONS("ADD_ALLOWED_CONDITIONS", 102),
        DELETE_DUTY_STATUS("DELETE_DUTY_STATUS", 103),
        DELETE_ALLOWED_CONDITIONS("DELETE_ALLOWED_CONDITIONS", 104);

        private String stringValue;
        private int intValue;

        EventTypeAction(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        @Override
        public String toString() {
            return stringValue;
        }

        public int toValue() {
            return intValue;
        }
    }

    public enum TransferMotive {
        UNKNOWN, UNKNOWN1, ADD_EVENT, UPDATE_DRIVER_ACUM, SHIPMENT, ADD_VIOLATION, UPDATE_DRIVER, UPDATE_DASHBOARD, LOGOUT, ADD_EDITED_EVENT, IFTA_MILEAGE, IFTA_FUEL,
        DELETE_VIOLATIONS, FMCSA_WEBSERVICE_RESPONSE, BLUETOOTH_DEVICE_SELECTION, ADD_EVENT_BY_LOCATION, ADD_EVENT_BY_ECM, UPDATE_TOKEN, UPDATE_TRACTOR_VIN, DTC_REPORT, DOWNLOAD_DRIVERS_CMV, UPDATE_DRIVER_ACUM_DASHBOARD, DOWNLOAD_DRIVERS_EVENTS_CMV, ADD_ASSETS, DOWNLOAD_DATA_FOR_UPDATE_VERSION, DOWNLOAD_TRANSFERS_BY_DRIVER, CONFIRMATION_DOWNLOAD_TRANSFERS_BY_DRIVER,
        ADD_CARRIER_EDIT_EVENT, LOGOUT_DVIR, DOWNLOAD_DATE_FROM_SERVER
    }

    public enum EventStatus {
        UNKNOWN, ACTIVE, INACTIVE_CHANGED, INACTIVE_CHANGED_REQUESTED, INACTIVE_CHANGED_REJECTED
    }

    public enum EventOrigin {
        UNKNOWN, AUTOMATICALLY_RECORDED, EDITED_ENTERED_BY_DRIVER, EDIT_REQUESTED_BY_OTHER_DRIVER, ASSUMED_FROM_UNIDENTIFIED_DRIVER
    }

    public enum ScheduleType {
        UNKNOWN, POSSIBLE_VIOLATION, INTERMDIATE_LOG, DASHBOARD, VIOLATION, END_OF_DAY, POWER_DIAGNOSTIC, ENGINE_SYNC_DIAGNOSTIC,
        UNIDENTIFIED_DRIVING_RECORDS_DIAGNOSTIC, POWER_MALFUNCTION, ENGINE_SYNC_MALFUNCTION, UPGRADE_FIRMWARE, DTC_REPORT, UPDATE_DATE
    }

    public enum Starting24hTime {
        MIDNIGHT, NOW
    }


    public enum ECMBusType {
        UNKNOWN, ODBII, J1708, J1939
    }

    public enum DTCStatus {
        UNKNOWN, NEW, DELETE
    }

    public enum TransferType {
        UNKNOWN, ADD_EVENT, EDIT_EVENT, DELETE_EVENT
    }

    public enum MalfunctionCode {
        POWER("P", "Power compliance"),
        ENGINE_SYNC("E", "Engine Synchronization compliance"),
        TIMING("T", "Timing compliance"),
        POSITIONING("L", "Positioning compliance"),
        DATA_RECORDING("R", "Data recording compliance"),
        DATA_TRANSFER("S", "Data transfer compliance"),
        OTHER_ELD("O", "Other ELD detected"),
        ;
        String code;
        String value;

        MalfunctionCode(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum DiagnosticCode {
        POWER("1", "Power data diagnostic"),
        ENGINE_SYNC("2", "Engine synchronization data diagnostic"),
        MISSING_DATA("3", "Missing data data diagnostic"),
        DATA_TRANSFER("4", "Data transfer data diagnostic"),
        UNIDENTIFIED_DRIVING_RECORDS("5", "Unidentified driving records data diagnostic"),
        OTHER_ELD("6", "Other ELD identified diagnostic");

        String code;
        String value;

        DiagnosticCode(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }

    public enum SystemAlertMotive {RESERVED, EXEMPTION_DRIVER, UNIDENTIFIED_MOVEMENT, MIDNIGHT_NOW, MOVEMENT}

    public static final int CHECK_IDLE_RUN_TIME = 60;//seconds
    public static final int GPS_DISTANCE_FOR_ADDRESS = 8000; //5 millas en metros
    public static final double START_ENGINE_LIMIT = 350;
    public static final int BATTERY_LEVEL = 15; //%
    public static final long STORAGE = 1048576; //1 MB

    //para validar si los valores del ecm son validos
    public static final long ECM_RPM_TIME = 120; //2 Min
    public static final long ECM_SPEED_TIME = 120; //2 Min

    public static final long ECM_ENG_HOURS_TIME = 600; //10 Min
    public static final long ECM_ODOMETER_TIME = 300; //5 Min
    public static final long ECM_OFFSET_UPDATE_TIME = 3600; //1 Hour

    public static final int ALARM_SERVICE_FRECUENCY = 15; //seconds
    public static final int DASHBOARD_FRECUENCY = 1200; //seconds. 1200 second = 20 min
    public static final int INTERMEDIATE_LOG_FRECUENCY = 3600;//seconds
    public static final int TRANSFER_SERVICE_FRECUENCY = 60;//seconds	900 second = 15 min
    public static final int GPS_LOCATION_FRECUENCY = 60;//seconds
    public static final int UPGRADE_FIRMWARE_FRECUENCY = 86400; // 24 hrs
    public static final int DTC_REPORT_FRECUENCY = 900;//15 minutes
    public static final int DTC_DELETE_FRECUENCY = 86400;  //24 hrs
    public static final int UPDATE_DATE_FRECUENCY = 43200;  //12 hrs
    //public static final String ONDUTY_ANNOTATION = "ONDUTY_ANNOTATION";
    public static final String ECM_CONNECTION_REPORTED = "ECM_CONNECTION_REPORTED";
    public static final String STORAGE_PATH = Environment.getExternalStorageDirectory() + "/HOSFolder";
    public static final String LAUNCHER_WIDGET = "Launcher_Widget";
    public static final int REQUEST_WIDGET = 246;
    public static final int REQUEST_CODE_VEHICLE_PROFILE = 200;
    public static final int RESULT_CODE_VEHICLE_PROFILE = 300;
    public static final String ACTION_SELECT_TRACTOR = "action_select_tractor";
    public static final String ACTION_SELECT_TRAILER = "action_select_trailer";
    public static final String TRACTOR_LIST = "tractor_list";
    public static final String TRAILER_LIST = "trailer_list";
    public static final String TRACTOR_SELECTED = "tractor_selected";
    public static final String TRAILER_SELECTED = "trailer_selected";
    public static final String BUNDLE_PATH_ORIGIN = "origin";
    public static final String PATH_FROM_LOGIN = "login";
    public static final String PATH_FROM_SUPPORT = "support";
    public static final String PATH_FROM_PRINCIPAL = "principal";
    public static final String PATH_FROM_VEHICLE_PROFILE = "vehicle_profile";
    public static final String PATH_FROM_DEVICE_LIST = "devices_list";
    public static final String PATH_FROM_DIAGNOSTIC = "diagnostic";
    public static final String PATH_FROM_BOOT_RECEIVER = "bootReceiver";
    public static final String PATH_FROM_TRANSFER = "transfer";

    public enum EventTypeWidget {
        ON, OFF, D, SB, PS, YM, PU
    }

    //FMCSA variables
    public static final String APOLLO_PRIVATE_KEY_PASSWORD = "A$$uredTracking17";
    public static final String REFRESH_PRINCIPAL_VIEW_ACTION = "refreshPrincipalView";
    public static final String UPDATE_SHIPMENT_VIEW_ACTION = "updateShipmentView";
    public static final String REFRESH_SERVICE_VIEW_ACTION = "updateServiceView";

    //Add log codes
    public static final int CREATE_EVENT = 200;
    public static final int EVENT_BEFORE_CYCLE_DAYS = 500;
    public static final int EVENT_EQUAL_TO_BEFORE = 502;
    public static final int EVENT_EQUAL_TO_AFTER = 503;
    public static final int EVENT_FORMAT_ERROR_LOCATION = 504;
    public static final int EVENT_FORMAT_ERROR_REASON = 505;
    public static final int EVENT_LATER_THAN_CURRENT_TIME = 506;

    //Rule set
    public static final int USA_60H = 0;
    public static final int USA_70H = 1;
    public static final int CALIFORNIA_80H = 2;
    public static final int OILFIELD_60H = 3;
    public static final int OILFIELD_70H = 4;
    public static final int CANADA_70H = 5;
    public static final int CANADA_120H = 6;
    public static final int TEXAS_70H = 7;
    public static final int MEXICO = 8;
    public static final int PASSENGER_60H = 9;
    public static final int PASSENGER_70H = 10;
    public static final int FLORIDA_70H = 11;
    public static final int FLORIDA_80H = 12;
    public static final int ASPHALT_60H = 13;
    public static final int ASPHALT_70H = 14;
    public static final int CALIFORNIA_TANKER_60H = 15;
    public static final int TEXAS_OILFIELD_70H = 16;
    public static final int ALASKA_70H = 17;
    public static final int ALASKA_80H = 18;

    //notifications id
    public static final int SERVICE_NOTIFICATION_ID = 1;
    public static final int LOCK_DRIVER_NOTIFICATION_ID = 2;
    public static final int REMOTE_NOTIFICATION_NOTIFICATION_ID = 3;

    //update view for when return ECM
    public static final String REFRESH_VIEW_ECM_ACTION = "refreshViewECM";

    //Type of DVIR
    public enum DVIRType {
        UNITED_STATES, CANADA_SCHEDULE_1, CANADA_QUEBEC_LIST_1, CANADA_QUEBEC_LIST_2, CANADA_QUEBEC_LIST_3
    }

    //Language of DVIR
    public enum DVIRLanguage {
        en, fr, es
    }


    //Type of Transfers
    public enum TypeTaskTransfer {
        UNLOAD, DOWNLOAD
    }

    //Sub-motive of Motive 24 for Version Update
    public enum TransferSubMotive {
        UNKNOWN, MISSING_ASSET_INFO, RESTORED_REQUEST_EDIT_SERVER
    }
}
