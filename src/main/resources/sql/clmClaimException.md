sample
===
* 注释###

    select #use("cols")# from CLM_CLAIM_EXCEPTION  where  #use("condition")#

cols
===
	REGISTNO,CLAIMNO,POLICYNO,RISKCODE,COMPANYCODE,ADJUSTESTTIMES,CLIENTCODE,CLIENTTYPE,OLDINDEMNITYDUTY,INDEMNITYDUTY,OLDINDEMNITYDUTYRATE,INDEMNITYDUTYRATE,OLDSUMESTLOSS,SUMESTLOSS,OLDESTLOSS,ESTLOSS,EXCEPTIONFLAG,EXCEPTIONCODE,DATE_CREATED,CREATED_BY,DATE_UPDATED,UPDATED_BY

updateSample
===
    
	REGISTNO=#registno#,CLAIMNO=#claimno#,POLICYNO=#policyno#,RISKCODE=#riskcode#,COMPANYCODE=#companycode#,ADJUSTESTTIMES=#adjustesttimes#,CLIENTCODE=#clientcode#,CLIENTTYPE=#clienttype#,OLDINDEMNITYDUTY=#oldindemnityduty#,INDEMNITYDUTY=#indemnityduty#,OLDINDEMNITYDUTYRATE=#oldindemnitydutyrate#,INDEMNITYDUTYRATE=#indemnitydutyrate#,OLDSUMESTLOSS=#oldsumestloss#,SUMESTLOSS=#sumestloss#,OLDESTLOSS=#oldestloss#,ESTLOSS=#estloss#,EXCEPTIONFLAG=#exceptionflag#,EXCEPTIONCODE=#exceptioncode#,DATE_CREATED=#dateCreated#,CREATED_BY=#createdBy#,DATE_UPDATED=#dateUpdated#,UPDATED_BY=#updatedBy#

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
    @if(!isEmpty(adjustesttimes)){
     and ADJUSTESTTIMES=#adjustesttimes#
    @}
    @if(!isEmpty(clientcode)){
     and CLIENTCODE=#clientcode#
    @}
    @if(!isEmpty(clienttype)){
     and CLIENTTYPE=#clienttype#
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
