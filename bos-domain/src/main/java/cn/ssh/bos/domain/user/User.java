package cn.ssh.bos.domain.user;
// Generated 2017-7-31 15:26:27 by Hibernate Tools 3.2.2.GA


import cn.ssh.bos.domain.auth.Role;
import cn.ssh.bos.domain.qp.NoticeBill;
import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * User generated by hbm2java
 */
@Entity
@Table(name = "t_user"
        , catalog = "day15"
        , uniqueConstraints = {@UniqueConstraint(columnNames = "TELEPHONE"), @UniqueConstraint(columnNames = "EMAIL")}
)
public class User implements java.io.Serializable {


    private Integer id;
    private Date birthday;
    private String email;
    private String gender;
    private String password;
    private String remark;
    private Long salary;
    private String station;
    private String telephone;
    private Set<NoticeBill> noticeBills = new HashSet<NoticeBill>(0);
    private Set<Role> roles = new HashSet<Role>(0);

    public User() {
    }


    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(Date birthday, String email, String gender, String password, String remark, Long salary, String station, String telephone, Set<NoticeBill> noticeBills, Set<Role> roles) {
        this.birthday = birthday;
        this.email = email;
        this.gender = gender;
        this.password = password;
        this.remark = remark;
        this.salary = salary;
        this.station = station;
        this.telephone = telephone;
        this.noticeBills = noticeBills;
        this.roles = roles;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTHDAY", length = 0)
    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Column(name = "EMAIL", unique = true, nullable = false, length = 30)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "GENDER", length = 10)
    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Column(name = "PASSWORD", nullable = false, length = 32)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "REMARK")
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "SALARY", precision = 10, scale = 0)
    public Long getSalary() {
        return this.salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    @Column(name = "STATION", length = 40)
    public String getStation() {
        return this.station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    @Column(name = "TELEPHONE", unique = true, length = 11)
    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @OneToMany( fetch = FetchType.LAZY, mappedBy = "user")
    @JSONField(serialize = false)
    public Set<NoticeBill> getNoticeBills() {
        return this.noticeBills;
    }

    public void setNoticeBills(Set<NoticeBill> noticeBills) {
        this.noticeBills = noticeBills;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", catalog = "day15", joinColumns = {
            @JoinColumn(name = "user_id", nullable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "role_id", nullable = false, updatable = false)})
    @JSONField(serialize = false)
    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


}


