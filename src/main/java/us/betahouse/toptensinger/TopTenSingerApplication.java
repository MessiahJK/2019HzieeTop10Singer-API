package us.betahouse.toptensinger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import us.betahouse.toptensinger.service.CommonService;

import javax.annotation.PostConstruct;

/**
 * @author MessiahJK
 */
@EnableJpaAuditing
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
@SpringBootApplication
public class TopTenSingerApplication {



    @Autowired
    public TopTenSingerApplication(CommonService commonService) {
        commonService.init();
    }


    public static void main(String[] args) {
        SpringApplication.run(TopTenSingerApplication.class, args);
    }


}
