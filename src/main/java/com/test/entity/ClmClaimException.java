package com.test.entity;
import java.math.*;
import java.util.Date;
import java.sql.Timestamp;
import org.beetl.sql.core.annotatoin.Table;


/* 
* 
* gen by beetlsql 2020-11-23
*/
@Table(name=".clm_claim_exception")
public class ClmClaimException   {
	
	// alias
	public static final String ALIAS_ADJUSTESTTIMES = "ADJUSTESTTIMES";
	public static final String ALIAS_CLAIMNO = "CLAIMNO";
	public static final String ALIAS_CLIENTCODE = "CLIENTCODE";
	public static final String ALIAS_CLIENTTYPE = "CLIENTTYPE";
	public static final String ALIAS_COMPANYCODE = "COMPANYCODE";
	public static final String ALIAS_CREATED_BY = "CREATED_BY";
	public static final String ALIAS_ESTLOSS = "ESTLOSS";
	public static final String ALIAS_EXCEPTIONCODE = "EXCEPTIONCODE";
	public static final String ALIAS_EXCEPTIONFLAG = "EXCEPTIONFLAG";
	public static final String ALIAS_INDEMNITYDUTY = "INDEMNITYDUTY";
	public static final String ALIAS_INDEMNITYDUTYRATE = "INDEMNITYDUTYRATE";
	public static final String ALIAS_OLDESTLOSS = "OLDESTLOSS";
	public static final String ALIAS_OLDINDEMNITYDUTY = "OLDINDEMNITYDUTY";
	public static final String ALIAS_OLDINDEMNITYDUTYRATE = "OLDINDEMNITYDUTYRATE";
	public static final String ALIAS_OLDSUMESTLOSS = "OLDSUMESTLOSS";
	public static final String ALIAS_POLICYNO = "POLICYNO";
	public static final String ALIAS_REGISTNO = "REGISTNO";
	public static final String ALIAS_RISKCODE = "RISKCODE";
	public static final String ALIAS_SUMESTLOSS = "SUMESTLOSS";
	public static final String ALIAS_UPDATED_BY = "UPDATED_BY";
	public static final String ALIAS_DATE_CREATED = "DATE_CREATED";
	public static final String ALIAS_DATE_UPDATED = "DATE_UPDATED";
	
	private Long adjustesttimes ;
	private String claimno ;
	private String clientcode ;
	private String clienttype ;
	private String companycode ;
	private String createdBy ;
	private BigDecimal estloss ;
	private String exceptioncode ;
	private String exceptionflag ;
	private String indemnityduty ;
	private Long indemnitydutyrate ;
	private BigDecimal oldestloss ;
	private String oldindemnityduty ;
	private Long oldindemnitydutyrate ;
	private BigDecimal oldsumestloss ;
	private String policyno ;
	private String registno ;
	private String riskcode ;
	private BigDecimal sumestloss ;
	private String updatedBy ;
	private Date dateCreated ;
	private Date dateUpdated ;
	
	public ClmClaimException() {
	}
	
	public Long getAdjustesttimes(){
		return  adjustesttimes;
	}
	public void setAdjustesttimes(Long adjustesttimes ){
		this.adjustesttimes = adjustesttimes;
	}
	
	public String getClaimno(){
		return  claimno;
	}
	public void setClaimno(String claimno ){
		this.claimno = claimno;
	}
	
	public String getClientcode(){
		return  clientcode;
	}
	public void setClientcode(String clientcode ){
		this.clientcode = clientcode;
	}
	
	public String getClienttype(){
		return  clienttype;
	}
	public void setClienttype(String clienttype ){
		this.clienttype = clienttype;
	}
	
	public String getCompanycode(){
		return  companycode;
	}
	public void setCompanycode(String companycode ){
		this.companycode = companycode;
	}
	
	public String getCreatedBy(){
		return  createdBy;
	}
	public void setCreatedBy(String createdBy ){
		this.createdBy = createdBy;
	}
	
	public BigDecimal getEstloss(){
		return  estloss;
	}
	public void setEstloss(BigDecimal estloss ){
		this.estloss = estloss;
	}
	
	public String getExceptioncode(){
		return  exceptioncode;
	}
	public void setExceptioncode(String exceptioncode ){
		this.exceptioncode = exceptioncode;
	}
	
	public String getExceptionflag(){
		return  exceptionflag;
	}
	public void setExceptionflag(String exceptionflag ){
		this.exceptionflag = exceptionflag;
	}
	
	public String getIndemnityduty(){
		return  indemnityduty;
	}
	public void setIndemnityduty(String indemnityduty ){
		this.indemnityduty = indemnityduty;
	}
	
	public Long getIndemnitydutyrate(){
		return  indemnitydutyrate;
	}
	public void setIndemnitydutyrate(Long indemnitydutyrate ){
		this.indemnitydutyrate = indemnitydutyrate;
	}
	
	public BigDecimal getOldestloss(){
		return  oldestloss;
	}
	public void setOldestloss(BigDecimal oldestloss ){
		this.oldestloss = oldestloss;
	}
	
	public String getOldindemnityduty(){
		return  oldindemnityduty;
	}
	public void setOldindemnityduty(String oldindemnityduty ){
		this.oldindemnityduty = oldindemnityduty;
	}
	
	public Long getOldindemnitydutyrate(){
		return  oldindemnitydutyrate;
	}
	public void setOldindemnitydutyrate(Long oldindemnitydutyrate ){
		this.oldindemnitydutyrate = oldindemnitydutyrate;
	}
	
	public BigDecimal getOldsumestloss(){
		return  oldsumestloss;
	}
	public void setOldsumestloss(BigDecimal oldsumestloss ){
		this.oldsumestloss = oldsumestloss;
	}
	
	public String getPolicyno(){
		return  policyno;
	}
	public void setPolicyno(String policyno ){
		this.policyno = policyno;
	}
	
	public String getRegistno(){
		return  registno;
	}
	public void setRegistno(String registno ){
		this.registno = registno;
	}
	
	public String getRiskcode(){
		return  riskcode;
	}
	public void setRiskcode(String riskcode ){
		this.riskcode = riskcode;
	}
	
	public BigDecimal getSumestloss(){
		return  sumestloss;
	}
	public void setSumestloss(BigDecimal sumestloss ){
		this.sumestloss = sumestloss;
	}
	
	public String getUpdatedBy(){
		return  updatedBy;
	}
	public void setUpdatedBy(String updatedBy ){
		this.updatedBy = updatedBy;
	}
	
	public Date getDateCreated(){
		return  dateCreated;
	}
	public void setDateCreated(Date dateCreated ){
		this.dateCreated = dateCreated;
	}
	
	public Date getDateUpdated(){
		return  dateUpdated;
	}
	public void setDateUpdated(Date dateUpdated ){
		this.dateUpdated = dateUpdated;
	}
	

}
