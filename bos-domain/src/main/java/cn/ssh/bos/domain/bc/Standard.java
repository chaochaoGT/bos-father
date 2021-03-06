package cn.ssh.bos.domain.bc;
// Generated 2017-7-17 20:38:59 by Hibernate Tools 3.2.2.GA


import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Standard generated by hbm2java
 */
@Entity
@Table(name="t_standard"
    ,catalog="day15"
    , uniqueConstraints = @UniqueConstraint(columnNames="NAME") 
)
public class Standard  implements java.io.Serializable {


     private Integer id;
     private String name;
     private Integer minweight;
     private Integer maxweight;
     private Integer minlength;
     private Integer maxlength;
     private Date operationtime;
     private String operator;
     private String operatorcompany;
     private Integer deltag=1;

    public Standard() {
    }

    public Standard(Date operationtime) {
        this.operationtime = operationtime;
    }
   
    public Standard(String name, Integer minweight, Integer maxweight, Integer minlength, Integer maxlength, Date operationtime, String operator, String operatorcompany, Integer deltag) {
       this.name = name;
       this.minweight = minweight;
       this.maxweight = maxweight;
       this.minlength = minlength;
       this.maxlength = maxlength;
       this.operationtime = operationtime;
       this.operator = operator;
       this.operatorcompany = operatorcompany;
       this.deltag = deltag;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="ID", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    @Column(name="NAME", unique=true, length=20)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="MINWEIGHT")
    public Integer getMinweight() {
        return this.minweight;
    }
    
    public void setMinweight(Integer minweight) {
        this.minweight = minweight;
    }
    
    @Column(name="MAXWEIGHT")
    public Integer getMaxweight() {
        return this.maxweight;
    }
    
    public void setMaxweight(Integer maxweight) {
        this.maxweight = maxweight;
    }
    
    @Column(name="MINLENGTH")
    public Integer getMinlength() {
        return this.minlength;
    }
    
    public void setMinlength(Integer minlength) {
        this.minlength = minlength;
    }
    
    @Column(name="MAXLENGTH")
    public Integer getMaxlength() {
        return this.maxlength;
    }
    
    public void setMaxlength(Integer maxlength) {
        this.maxlength = maxlength;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="OPERATIONTIME", nullable=false)
    public Date getOperationtime() {
        return this.operationtime;
    }
    
    public void setOperationtime(Date operationtime) {
        this.operationtime = operationtime;
    }
    
    @Column(name="OPERATOR", length=20)
    public String getOperator() {
        return this.operator;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
    
    @Column(name="OPERATORCOMPANY")
    public String getOperatorcompany() {
        return this.operatorcompany;
    }
    
    public void setOperatorcompany(String operatorcompany) {
        this.operatorcompany = operatorcompany;
    }
    
    @Column(name="DELTAG")
    public Integer getDeltag() {
        return this.deltag;
    }
    
    public void setDeltag(Integer deltag) {
        this.deltag = deltag;
    }




}


