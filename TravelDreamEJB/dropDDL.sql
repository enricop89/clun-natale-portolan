ALTER TABLE COMPONENTS_HELPER DROP FOREIGN KEY FK_COMPONENTS_HELPER_TRAVELCOMPONENT_ID
ALTER TABLE COMPONENTS_HELPER DROP FOREIGN KEY COMPONENTS_HELPER_PERSONALIZED_TRAVEL_PACKAGE_ID
ALTER TABLE COMPONENTS_HELPER DROP FOREIGN KEY FK_COMPONENTS_HELPER_TRAVELELEMENT_ID
ALTER TABLE GIFTELEMENTS_HELPER DROP FOREIGN KEY GIFTELEMENTS_HELPER_PERSONALIZEDTRAVELPACKAGE_ID
ALTER TABLE GIFTELEMENTS_HELPER DROP FOREIGN KEY FK_GIFTELEMENTS_HELPER_GIFT_LIST_ID
ALTER TABLE GIFTELEMENTS_HELPER DROP FOREIGN KEY FK_GIFTELEMENTS_HELPER_TRAVELCOMPONENT_ID
ALTER TABLE GIFTLIST DROP FOREIGN KEY FK_GIFTLIST_OWNER_USERNAME
ALTER TABLE PERSONALIZEDTRAVELPACKAGE DROP FOREIGN KEY FK_PERSONALIZEDTRAVELPACKAGE_OWNER_USERNAME
ALTER TABLE TRAVELELEMENT DROP FOREIGN KEY FK_TRAVELELEMENT_TRAVEL_COMPONENT_ID
ALTER TABLE TRAVELELEMENT DROP FOREIGN KEY FK_TRAVELELEMENT_OWNER_USERNAME
ALTER TABLE USER_GROUP DROP FOREIGN KEY FK_USER_GROUP_USERNAME
ALTER TABLE USER_GROUP DROP FOREIGN KEY FK_USER_GROUP_GROUPNAME
ALTER TABLE PREDEFINED_COMPONENT DROP FOREIGN KEY FK_PREDEFINED_COMPONENT_PREDEFINED_ID
ALTER TABLE PREDEFINED_COMPONENT DROP FOREIGN KEY FK_PREDEFINED_COMPONENT_COMPONENT_ID
DROP TABLE COMPONENTS_HELPER
DROP TABLE GIFTELEMENTS_HELPER
DROP TABLE GIFTLIST
DROP TABLE GROUPS
DROP TABLE PERSONALIZEDTRAVELPACKAGE
DROP TABLE PREDEFINEDTRAVELPACKAGE
DROP TABLE TRAVELCOMPONENT
DROP TABLE TRAVELELEMENT
DROP TABLE USERS
DROP TABLE USER_GROUP
DROP TABLE PREDEFINED_COMPONENT
