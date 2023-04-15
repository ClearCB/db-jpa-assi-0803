package edu.craptocraft.assibdjpamariadb.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "Users")
public class Users implements Data {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "registration_date")
    private String registration_date;

    public Users() {
    }

    public Users(Integer id, String username, String password, String email, String registration_date) {

        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.registration_date = registration_date;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(String registration_date) {
        this.registration_date = registration_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Users that = (Users) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void print() {

        System.out.println("\n\t>" + this.getUsername() + ":" + this.getEmail());

    }

    @Override
    public void createData() {

        StringBuilder createStatement = new StringBuilder();

        createStatement.append(
                "insert into Users (id, username, password, email, registration_date) values (?,?,?,?,?)");

        JpaService.getInstance().runInTransaction(entityManager -> entityManager.createNativeQuery(
                createStatement.toString())
                .setParameter(1, this.getId())
                .setParameter(2, this.getUsername())
                .setParameter(3, this.getPassword())
                .setParameter(4, this.getEmail())
                .setParameter(5, this.getRegistration_date())
                .executeUpdate());

    }

    @Override
    public void updateData(){

        StringBuilder createStatement = new StringBuilder();

        createStatement.append(
                "update Users set username = ? , password = ? , email = ? , registration_date = ? where id = ?"
        );

        JpaService.getInstance().runInTransaction(entityManager -> entityManager.createNativeQuery(
                createStatement.toString())
                .setParameter(1, this.getUsername())
                .setParameter(2 , this.getPassword())
                .setParameter(3, this.getEmail())
                .setParameter(4, this.getRegistration_date())
                .setParameter(5, this.getId())
                .executeUpdate()
        );

    }
}