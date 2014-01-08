CREATE TABLE COMPONENTS_HELPER (ID BIGINT AUTO_INCREMENT NOT NULL, PERSONALIZED_TRAVEL_PACKAGE_ID BIGINT, TRAVELCOMPONENT_ID BIGINT, TRAVELELEMENT_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE GIFTELEMENTS_HELPER (ID BIGINT NOT NULL, GIFT_LIST_ID VARCHAR(255), PERSONALIZEDTRAVELPACKAGE_ID BIGINT, TRAVELCOMPONENT_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE GIFTLIST (OWNER_EMAIL VARCHAR(255) NOT NULL, PRIMARY KEY (OWNER_EMAIL))
CREATE TABLE GROUPS (GROUPNAME VARCHAR(255) NOT NULL, PRIMARY KEY (GROUPNAME))
CREATE TABLE PERSONALIZEDTRAVELPACKAGE (ID BIGINT AUTO_INCREMENT NOT NULL, NAME VARCHAR(255), OWNER_EMAIL VARCHAR(255) NOT NULL, PRIMARY KEY (ID))
CREATE TABLE PREDEFINEDTRAVELPACKAGE (ID BIGINT AUTO_INCREMENT NOT NULL, DESCRIPTION VARCHAR(255), NAME VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE TRAVELCOMPONENT (ID BIGINT AUTO_INCREMENT NOT NULL, EXCURSIONCITY VARCHAR(255), EXCURSIONDATETIME DATETIME, EXCURSIONDESCRIPTION VARCHAR(255), FLIGHTARRIVALCITY VARCHAR(255), FLIGHTARRIVALDATETIME DATETIME, FLIGHTCODE VARCHAR(255), FLIGHTDEPARTURECITY VARCHAR(255), FLIGHTDEPARTUREDATETIME DATETIME, HOTELCITY VARCHAR(255), HOTELDATE DATE, SUPPLYINGCOMPANY VARCHAR(255), TYPE VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE TRAVELELEMENT (ID BIGINT AUTO_INCREMENT NOT NULL, CONFIRMATIONDATETIME DATETIME, TRAVEL_COMPONENT_ID BIGINT, OWNER_EMAIL VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE USERS (EMAIL VARCHAR(255) NOT NULL, FIRSTNAME VARCHAR(255), LASTNAME VARCHAR(255), LOGINFO VARCHAR(255), PASSWORD VARCHAR(255), USERNAME VARCHAR(255), PRIMARY KEY (EMAIL))
CREATE TABLE USER_GROUP (GROUPNAME VARCHAR(255) NOT NULL, EMAIL VARCHAR(255) NOT NULL, PRIMARY KEY (GROUPNAME, EMAIL))
CREATE TABLE PREDEFINED_COMPONENT (PREDEFINED_ID BIGINT NOT NULL, COMPONENT_ID BIGINT NOT NULL, PRIMARY KEY (PREDEFINED_ID, COMPONENT_ID))
ALTER TABLE COMPONENTS_HELPER ADD CONSTRAINT FK_COMPONENTS_HELPER_TRAVELCOMPONENT_ID FOREIGN KEY (TRAVELCOMPONENT_ID) REFERENCES TRAVELCOMPONENT (ID)
ALTER TABLE COMPONENTS_HELPER ADD CONSTRAINT COMPONENTS_HELPER_PERSONALIZED_TRAVEL_PACKAGE_ID FOREIGN KEY (PERSONALIZED_TRAVEL_PACKAGE_ID) REFERENCES PERSONALIZEDTRAVELPACKAGE (ID)
ALTER TABLE COMPONENTS_HELPER ADD CONSTRAINT FK_COMPONENTS_HELPER_TRAVELELEMENT_ID FOREIGN KEY (TRAVELELEMENT_ID) REFERENCES TRAVELELEMENT (ID)
ALTER TABLE GIFTELEMENTS_HELPER ADD CONSTRAINT GIFTELEMENTS_HELPER_PERSONALIZEDTRAVELPACKAGE_ID FOREIGN KEY (PERSONALIZEDTRAVELPACKAGE_ID) REFERENCES PERSONALIZEDTRAVELPACKAGE (ID)
ALTER TABLE GIFTELEMENTS_HELPER ADD CONSTRAINT FK_GIFTELEMENTS_HELPER_GIFT_LIST_ID FOREIGN KEY (GIFT_LIST_ID) REFERENCES GIFTLIST (OWNER_EMAIL)
ALTER TABLE GIFTELEMENTS_HELPER ADD CONSTRAINT FK_GIFTELEMENTS_HELPER_TRAVELCOMPONENT_ID FOREIGN KEY (TRAVELCOMPONENT_ID) REFERENCES TRAVELCOMPONENT (ID)
ALTER TABLE GIFTLIST ADD CONSTRAINT FK_GIFTLIST_OWNER_EMAIL FOREIGN KEY (OWNER_EMAIL) REFERENCES USERS (EMAIL)
ALTER TABLE PERSONALIZEDTRAVELPACKAGE ADD CONSTRAINT FK_PERSONALIZEDTRAVELPACKAGE_OWNER_EMAIL FOREIGN KEY (OWNER_EMAIL) REFERENCES USERS (EMAIL)
ALTER TABLE TRAVELELEMENT ADD CONSTRAINT FK_TRAVELELEMENT_TRAVEL_COMPONENT_ID FOREIGN KEY (TRAVEL_COMPONENT_ID) REFERENCES TRAVELCOMPONENT (ID)
ALTER TABLE TRAVELELEMENT ADD CONSTRAINT FK_TRAVELELEMENT_OWNER_EMAIL FOREIGN KEY (OWNER_EMAIL) REFERENCES USERS (EMAIL)
ALTER TABLE USER_GROUP ADD CONSTRAINT FK_USER_GROUP_GROUPNAME FOREIGN KEY (GROUPNAME) REFERENCES GROUPS (GROUPNAME)
ALTER TABLE USER_GROUP ADD CONSTRAINT FK_USER_GROUP_EMAIL FOREIGN KEY (EMAIL) REFERENCES USERS (EMAIL)
ALTER TABLE PREDEFINED_COMPONENT ADD CONSTRAINT FK_PREDEFINED_COMPONENT_PREDEFINED_ID FOREIGN KEY (PREDEFINED_ID) REFERENCES PREDEFINEDTRAVELPACKAGE (ID)
ALTER TABLE PREDEFINED_COMPONENT ADD CONSTRAINT FK_PREDEFINED_COMPONENT_COMPONENT_ID FOREIGN KEY (COMPONENT_ID) REFERENCES TRAVELCOMPONENT (ID)
