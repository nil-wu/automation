sample
===
* 注释###

    select #use("cols")# from CLM_DUTY_CORRECT  where  #use("condition")#

cols
===
	REGISTNO,CLAIMNO,POLICYNO,RISKCODE,COMPANYCODE,TIMES,ADJUSTESTTIMES,OLDINDEMNITYDUTY,INDEMNITYDUTY,OLDINDEMNITYDUTYRATE,INDEMNITYDUTYRATE,OLDDEDUCTCONDRULE,DEDUCTCONDRULE,OLDSUMESTLOSS,SUMESTLOSS,OLDESTLOSS,ESTLOSS,STATUS,UNDERWRITEIND,OPERATORCODE,OPERATORDATE,OPERATORREASON,EXCEPTIONFLAG,EXCEPTIONCODE,DATE_CREATED,CREATED_BY,DATE_UPDATED,UPDATED_BY,SERIOUSNO

updateSample
===
    
	REGISTNO=#registno#,CLAIMNO=#claimno#,POLICYNO=#policyno#,RISKCODE=#riskcode#,COMPANYCODE=#companycode#,TIMES=#times#,ADJUSTESTTIMES=#adjustesttimes#,OLDINDEMNITYDUTY=#oldindemnityduty#,INDEMNITYDUTY=#indemnityduty#,OLDINDEMNITYDUTYRATE=#oldindemnitydutyrate#,INDEMNITYDUTYRATE=#indemnitydutyrate#,OLDDEDUCTCONDRULE=#olddeductcondrule#,DEDUCTCONDRULE=#deductcondrule#,OLDSUMESTLOSS=#oldsumestloss#,SUMESTLOSS=#sumestloss#,OLDESTLOSS=#oldestloss#,ESTLOSS=#estloss#,STATUS=#status#,UNDERWRITEIND=#underwriteind#,OPERATORCODE=#operatorcode#,OPERATORDATE=#operatordate#,OPERATORREASON=#operatorreason#,EXCEPTIONFLAG=#exceptionflag#,EXCEPTIONCODE=#exceptioncode#,DATE_CREATED=#dateCreated#,CREATED_BY=#createdBy#,DATE_UPDATED=#dateUpdated#,UPDATED_BY=#updatedBy#,SERIOUSNO=#seriousno#

condition
===

    1 = 1  
    @if(!isEmpty(registno)){
     and REGISTNO=#registno#
    @}
    @if(!isEmpty(claimno)){
     and CLAIMNO=#claimno#
    @}
    @if(!isEmpty(policyno)){
     and POLICYNO=#policyno#
    @}
    @if(!isEmpty(riskcode)){
     and RISKCODE=#riskcode#
    @}
    @if(!isEmpty(companycode)){
     and COMPANYCODE=#companycode#
    @}
    @if(!isEmpty(times)){
     and TIMES=#times#
    @}
    @if(!isEmpty(adjustesttimes)){
     and ADJUSTESTTIMES=#adjustesttimes#
    @}
    @if(!isEmpty(oldindemnityduty)){
     and OLDINDEMNITYDUTY=#oldindemnityduty#
    @}
    @if(!isEmpty(indemnityduty)){
     and INDEMNITYDUTY=#indemnityduty#
    @}
    @if(!isEmpty(oldindemnitydutyrate)){
     and OLDINDEMNITYDUTYRATE=#oldindemnitydutyrate#
    @}
    @if(!isEmpty(indemnitydutyrate)){
     and INDEMNITYDUTYRATE=#indemnitydutyrate#
    @}
    @if(!isEmpty(olddeductcondrule)){
     and OLDDEDUCTCONDRULE=#olddeductcondrule#
    @}
    @if(!isEmpty(deductcondrule)){
     and DEDUCTCONDRULE=#deductcondrule#
    @}
    @if(!isEmpty(oldsumestloss)){
     and OLDSUMESTLOSS=#oldsumestloss#
    @}
    @if(!isEmpty(sumestloss)){
     and SUMESTLOSS=#sumestloss#
    @}
    @if(!isEmpty(oldestloss)){
     and OLDESTLOSS=#oldestloss#
    @}
    @if(!isEmpty(estloss)){
     and ESTLOSS=#estloss#
    @}
    @if(!isEmpty(status)){
     and STATUS=#status#
    @}
    @if(!isEmpty(underwriteind)){
     and UNDERWRITEIND=#underwriteind#
    @}
    @if(!isEmpty(operatorcode)){
     and OPERATORCODE=#operatorcode#
    @}
    @if(!isEmpty(operatordate)){
     and OPERATORDATE=#operatordate#
    @}
    @if(!isEmpty(operatorreason)){
     and OPERATORREASON=#operatorreason#
    @}
    @if(!isEmpty(exceptionflag)){
     and EXCEPTIONFLAG=#exceptionflag#
    @}
    @if(!isEmpty(exceptioncode)){
     and EXCEPTIONCODE=#exceptioncode#
    @}
    @if(!isEmpty(dateCreated)){
     and DATE_CREATED=#dateCreated#
    @}
    @if(!isEmpty(createdBy)){
     and CREATED_BY=#createdBy#
    @}
    @if(!isEmpty(dateUpdated)){
     and DATE_UPDATED=#dateUpdated#
    @}
    @if(!isEmpty(updatedBy)){
     and UPDATED_BY=#updatedBy#
    @}
    @if(!isEmpty(seriousno)){
     and SERIOUSNO=#seriousno#
    @}
