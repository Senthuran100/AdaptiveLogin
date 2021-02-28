package com.example.login;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import com.example.login.model.UserLoginParam;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Value("${database.url}")
    private String databaseURL;

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        System.out.println("databaseURL "+databaseURL);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/login_app");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        return dataSource;
    }

    @Bean
    public JdbcCursorItemReader<UserLoginParam> reader(){
        JdbcCursorItemReader<UserLoginParam> reader = new JdbcCursorItemReader<UserLoginParam>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT username,datetime,browser,location,mouseevent,keyboardevent,browser_info FROM user_login_params");
        reader.setRowMapper(new UserRowMapper());

        return reader;
    }

    public class UserRowMapper implements RowMapper<UserLoginParam>{

        @Override
        public UserLoginParam mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserLoginParam user = new UserLoginParam();
            user.setUsername(rs.getString("username"));
            user.setDatetime(rs.getDate("datetime"));
            user.setBrowser(rs.getString("browser"));
            user.setLocation(rs.getString("location"));
            user.setMouseevent(rs.getString("mouseevent"));
            user.setKeyboardevent(rs.getString("keyboardevent"));
            user.setBrowser_info(rs.getString("browser_info"));
            return user;
        }

    }

    @Bean
    public UserItemProcessor processor(){
        return new UserItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<UserLoginParam> writer(){
        FlatFileItemWriter<UserLoginParam> writer = new FlatFileItemWriter<UserLoginParam>();
        writer.setResource(new ClassPathResource("LoginParam.csv"));
        writer.setLineAggregator(new DelimitedLineAggregator<UserLoginParam>() {{
            setDelimiter(",");
            setFieldExtractor(new BeanWrapperFieldExtractor<UserLoginParam>() {{
                setNames(new String[] { "username", "datetime","browser","location","mouseevent","keyboardevent","browser_info" });
            }});
        }});

        return writer;
    }



    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").<UserLoginParam, UserLoginParam> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job exportUserJob() {
        return jobBuilderFactory.get("exportUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();
    }
}
