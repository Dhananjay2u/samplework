--Start of comparison [EOPS_HUB_CB02.stepdefinition] Table, output is in form of DML Scripts : 
--**********************************************************************************
--Insert scripts --------------------------------------------------------------------
INSERT INTO EOPS_HUB_CB02.stepdefinition  ( COMPANYCODE , PROCESSID , NAME , ALLOWUPLOAD , UITEMPLATENAME , STEPTYPE , STEPMODE , CATEGORY , TAT , LABEL , DESCRIPTION , DISPLAYNAME , DISPLAYORDER , MAXLOCKEDTIME , MAXTASKROW , ABR_GROUP , CHRONOLOGICALORDER , DAPID , STEPCODE , AUTOCOMMENT , AUTOMOVEMENTFLAG , NOTIFICATIONFLAG , ESCALATIONFLAG , MULTISCREEN , ISTRACKENABLED , SUID , CNTBROWSE , TINSTANCE , TRIGER , ISREFERRAL , APPSTATUSRULE , STATREQUIRED , DYNASCREENID ) VALUES  ('SC','P869','RF_ReferralGroup4','N','Template1','M','Browse','MK','180','Diarizer','null','null','4','0','100','15','ORDER BY TP.priority DESC,TP.score DESC,T.ENQUEUETIME ASC','null','null','null','N','N','N','null','null','P869_RF_ReferralGroup4','null','null','null','null','null','null','null');
--Update scripts --------------------------------------------------------------------
UPDATE EOPS_HUB_CB02.stepdefinition SET  CATEGORY='14'  WHERE PROCESSID='P869' AND NAME='AR_SuppDocs' ;
UPDATE EOPS_HUB_CB02.stepdefinition SET  LABEL='Pre-Check'  WHERE PROCESSID='P869' AND NAME='CI1' ;
--Delete scripts --------------------------------------------------------------------
--End of comparison of [EOPS_HUB_CB02.stepdefinition] Table----------------------------------
