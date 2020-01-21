package com.bluesky.framework.api.mongodb;

import com.bluesky.framework.api.rabbitMQ.entity.Mail;
import com.bluesky.framework.api.rabbitMQ.service.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MongoController {

    @Autowired
    private MongoDao mtdao;
    @Autowired
    private Producer producer;

    @GetMapping(value="/test1")
    public void saveTest() throws Exception {
        MongoTest mgtest=new MongoTest();
        mgtest.setId(22);
        mgtest.setAge(33);
        mgtest.setName("ceshi");
        mtdao.saveTest(mgtest);
        Mail mail = Mail.builder()
                .mailId(mgtest.getId().toString())
                .country(mgtest.toString())
                .build();
        for(int i=0; i<10; i++) {
            producer.sendMail("myqueue", mail);
        }

    }

    @GetMapping(value="/test2")
    public MongoTest findTestByName(){
        MongoTest mgtest= mtdao.findTestByName("ceshi");
        System.out.println("mgtest is "+mgtest);
        return mgtest;
    }

    @GetMapping(value="/test3")
    public void updateTest(){
        MongoTest mgtest=new MongoTest();
        mgtest.setId(11);
        mgtest.setAge(44);
        mgtest.setName("ceshi2");
        mtdao.updateTest(mgtest);
    }

    @GetMapping(value="/test4")
    public void deleteTestById(){
        mtdao.deleteTestById(11);
    }
}
