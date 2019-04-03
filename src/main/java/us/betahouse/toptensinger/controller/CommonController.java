/*
  betahouse.us
  CopyRight (c) 2012 - 2019
 */
package us.betahouse.toptensinger.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import us.betahouse.toptensinger.constant.ScoreType;
import us.betahouse.toptensinger.dao.ContestRepository;
import us.betahouse.toptensinger.dao.PlayerRepository;
import us.betahouse.toptensinger.dao.ScoreRepository;
import us.betahouse.toptensinger.model.*;
import us.betahouse.toptensinger.socket.MyWebSocket;
import us.betahouse.toptensinger.socket.SocketMessage;
import us.betahouse.toptensinger.util.DictionaryUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author MessiahJK
 * @version : CommonController.java 2019/03/26 12:18 MessiahJK
 */
@RestController
public class CommonController {

    private final DictionaryUtil dictionaryUtil;
    private final ContestRepository contestRepository;
    private final ScoreRepository scoreRepository;
    private final PlayerRepository playerRepository;





    @Autowired
    public CommonController(DictionaryUtil dictionaryUtil, ContestRepository contestRepository, ScoreRepository scoreRepository, PlayerRepository playerRepository) {
        this.dictionaryUtil = dictionaryUtil;
        this.contestRepository = contestRepository;
        this.scoreRepository = scoreRepository;
        this.playerRepository = playerRepository;
    }

    @CrossOrigin
    @GetMapping("/contest")
    public List<Contest> getContestList(){
        return contestRepository.findAll();
    }

    @CrossOrigin
    @PutMapping("/score")
    public Boolean putScore(Long id,BigDecimal scoreValue,String type) throws IOException {
        if(!ScoreType.TEACHER.name().equals(type) &&!ScoreType.STUDENT.name().equals(type)){
            return false;
        }
        Score score=new Score();
        score.setContestId(dictionaryUtil.getActiveContest());
        score.setPlayerId(id);
        score.setValue(scoreValue);
        score.setType(type);
        scoreRepository.save(score);
        //socket
        SocketMessage socketMessage=new SocketMessage();
        socketMessage.setName("playerList");
        socketMessage.add("playerList", getPlayerList());
        MyWebSocket.sendInfo(socketMessage.toJSON());
        return true;
    }

    @CrossOrigin
    @GetMapping("/playerList")
    public List<CommonPlayer> getPlayerList(){
        Long activeContest=dictionaryUtil.getActiveContest();
        BigDecimal studentFractionRatio=dictionaryUtil.getStudentFractionRatio();
        BigDecimal teacherFractionRatio=dictionaryUtil.getTeacherFractionRatio();
        List<Player> playerList=playerRepository.findByContestId(activeContest);
        List<CommonPlayer> commonPlayerList=new ArrayList<>(16);
        List<Score> scoreList,studentScoreList,teacherScoreList;
        for(Player player:playerList){
            scoreList=scoreRepository.findByContestIdAndPlayerId(activeContest, player.getId());
            CommonPlayer commonPlayer=new CommonPlayer();
            commonPlayer.setName(player.getName());
            commonPlayer.setContestId(player.getContestId());
            commonPlayer.setId(player.getId());
            commonPlayer.setPlayerId(player.getPlayerId());
            commonPlayer.setNumber(scoreList.size());
            if(scoreList.size()!=0) {
                BigDecimal studentScore = BigDecimal.ZERO;
                BigDecimal teacherScore = BigDecimal.ZERO;
                studentScoreList = scoreRepository.findByContestIdAndPlayerIdAndType(activeContest, player.getId(), ScoreType.STUDENT.name());
                for (Score score : studentScoreList) {
                    studentScore = studentScore.add(score.getValue());
                }
                if (studentScoreList.size() != 0) {
                    studentScore = studentScore.divide(BigDecimal.valueOf(studentScoreList.size()),3,BigDecimal.ROUND_HALF_UP);
                }
                teacherScoreList = scoreRepository.findByContestIdAndPlayerIdAndType(activeContest, player.getId(), ScoreType.TEACHER.name());
                for (Score score : teacherScoreList) {
                    teacherScore = teacherScore.add(score.getValue());
                }
                if(teacherScoreList.size()!=0){
                    teacherScore=teacherScore.divide(BigDecimal.valueOf(teacherScoreList.size()),3,BigDecimal.ROUND_HALF_UP);
                }
                if(studentScore.equals(BigDecimal.ZERO)){
                    commonPlayer.setAverageScore(teacherScore);
                }else if(teacherScore.equals(BigDecimal.ZERO)){
                    commonPlayer.setAverageScore(studentScore);
                }else{
                    commonPlayer.setAverageScore(teacherScore.multiply(teacherFractionRatio).add(studentScore.multiply(studentFractionRatio)));
                }

            }else{
                commonPlayer.setAverageScore(BigDecimal.ZERO);
            }
            commonPlayerList.add(commonPlayer);
        }
        return  commonPlayerList;
    }

    @CrossOrigin
    @DeleteMapping("/score")
    public Boolean resetScore(Long id) throws IOException {
        if(id==null){
            return false;
        }
        Long activeContest=dictionaryUtil.getActiveContest();
        List<Score> scoreList=scoreRepository.findByContestIdAndPlayerId(activeContest, id);
        scoreRepository.deleteAll(scoreList);
        //socket
        SocketMessage socketMessage=new SocketMessage();
        socketMessage.setName("playerList");
        socketMessage.add("playerList", getPlayerList());
        MyWebSocket.sendInfo(socketMessage.toJSON());
        return true;
    }

    @CrossOrigin
    @PostMapping("/contest")
    public List<Contest> chooseContest(Long id){
        Long activeContest=dictionaryUtil.getActiveContest();
        //修改旧活动
        Contest contest=contestRepository.findById(activeContest).get();
        contest.setStatus("sleep");
        contestRepository.save(contest);
        //修改新活动
        Contest newActiveContest=contestRepository.findById(id).get();
        newActiveContest.setStatus("active");
        contestRepository.save(newActiveContest);
        //修改字典
        dictionaryUtil.updateActiveContest(newActiveContest.getId(), newActiveContest.getName());
        return getContestList();
    }

    @CrossOrigin
    @GetMapping("/player")
    public CommonPlayer getPlayer(Long id) throws IOException {
        Long activeContest=dictionaryUtil.getActiveContest();
        BigDecimal studentFractionRatio=dictionaryUtil.getStudentFractionRatio();
        BigDecimal teacherFractionRatio=dictionaryUtil.getTeacherFractionRatio();
        Player player=playerRepository.getOne(id);
        List<Score> scoreList,studentScoreList,teacherScoreList;
        scoreList=scoreRepository.findByContestIdAndPlayerId(activeContest, player.getId());

        CommonPlayer commonPlayer=new CommonPlayer();
        commonPlayer.setId(player.getId());
        commonPlayer.setPlayerId(player.getPlayerId());
        commonPlayer.setNumber(scoreList.size());
        commonPlayer.setName(player.getName());
        commonPlayer.setContestId(player.getContestId());
        if(scoreList.size()!=0) {
            BigDecimal studentScore = BigDecimal.ZERO;
            BigDecimal teacherScore = BigDecimal.ZERO;
            studentScoreList = scoreRepository.findByContestIdAndPlayerIdAndType(activeContest, player.getId(), ScoreType.STUDENT.name());
            for (Score score : studentScoreList) {
                studentScore = studentScore.add(score.getValue());
            }
            if (studentScoreList.size() != 0) {
                studentScore = studentScore.divide(BigDecimal.valueOf(studentScoreList.size()),3,BigDecimal.ROUND_HALF_UP);
            }
            teacherScoreList = scoreRepository.findByContestIdAndPlayerIdAndType(activeContest, player.getId(), ScoreType.TEACHER.name());
            for (Score score : teacherScoreList) {
                teacherScore = teacherScore.add(score.getValue());
            }
            if(teacherScoreList.size()!=0){
                teacherScore=teacherScore.divide(BigDecimal.valueOf(teacherScoreList.size()),3,BigDecimal.ROUND_HALF_UP);
            }
            if(studentScore.equals(BigDecimal.ZERO)){
                commonPlayer.setAverageScore(teacherScore);
            }else if(teacherScore.equals(BigDecimal.ZERO)){
                commonPlayer.setAverageScore(studentScore);
            }else{
                commonPlayer.setAverageScore(teacherScore.multiply(teacherFractionRatio).add(studentScore.multiply(studentFractionRatio)));
            }
        }else{
            commonPlayer.setAverageScore(BigDecimal.ZERO);
        }
        SocketMessage socketMessage=new SocketMessage();
        socketMessage.setName("player");
        socketMessage.add("player", commonPlayer);
        MyWebSocket.sendInfo(socketMessage.toJSON());
        return commonPlayer;
    }

    @CrossOrigin
    @GetMapping("/score")
    public ScoreResult getScoreResult(Long id){
        Long activeContest=dictionaryUtil.getActiveContest();
        List<Score> studentScoreList,teacherScoreList;
        studentScoreList = scoreRepository.findByContestIdAndPlayerIdAndType(activeContest, id, ScoreType.STUDENT.name());
        teacherScoreList= scoreRepository.findByContestIdAndPlayerIdAndType(activeContest, id, ScoreType.TEACHER.name());
        return new ScoreResult(studentScoreList, teacherScoreList);
    }
}
