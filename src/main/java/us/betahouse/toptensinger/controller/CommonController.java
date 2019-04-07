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
        score.setPlayer(playerRepository.getOne(id));
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
    public List<Player> getPlayerList(){
        Long activeContest=dictionaryUtil.getActiveContest();
        List<Player> playerList=playerRepository.findByContestId(activeContest);
        playerList.forEach(this::completeIt);
        return  playerList;
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
    public Player getPlayer(Long id) throws IOException {
        Player player=playerRepository.getOne(id);
        completeIt(player);
        SocketMessage socketMessage=new SocketMessage();
        socketMessage.setName("player");
        socketMessage.add("player", player);
        MyWebSocket.sendInfo(socketMessage.toJSON());
        return player;
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

    private void completeIt(Player player) {
        if (player.getScoreList() == null || player.getScoreList().size() == 0) {
            player.setNumber(0);
            player.setAverageScore(BigDecimal.ZERO);
        } else {
            player.setNumber(player.getScoreList().size());
            BigDecimal studentFractionRatio = dictionaryUtil.getStudentFractionRatio();
            BigDecimal teacherFractionRatio = dictionaryUtil.getTeacherFractionRatio();
            AtomicReference<BigDecimal> teacherScore = new AtomicReference<>();
            AtomicReference<BigDecimal> studentScore = new AtomicReference<>();
            teacherScore.set(BigDecimal.ZERO);
            studentScore.set(BigDecimal.ZERO);
            player.getScoreList().stream()
                    .filter(score -> score.getType().equals(ScoreType.TEACHER.name()))
                    .forEach(score -> teacherScore.set(teacherScore.get().add(score.getValue())));
            player.getScoreList().stream()
                    .filter(score -> score.getType().equals(ScoreType.STUDENT.name()))
                    .forEach(score -> teacherScore.set(teacherScore.get().add(score.getValue())));
            Long teacherNumber = player.getScoreList().stream()
                    .filter(score -> score.getType().equals(ScoreType.TEACHER.name()))
                    .count();
            Long studentNumber = player.getScoreList().size() - teacherNumber;
            if (teacherNumber == 0 && studentNumber == 0) {
                player.setAverageScore(BigDecimal.ZERO);
            } else if (studentNumber == 0) {
                player.setAverageScore(teacherScore.get().divide(BigDecimal.valueOf(teacherNumber), 3, BigDecimal.ROUND_HALF_UP));
            } else if (teacherNumber == 0) {
                player.setAverageScore(studentScore.get().divide(BigDecimal.valueOf(studentNumber), 3, BigDecimal.ROUND_HALF_UP));
            } else {
                player.setAverageScore(teacherScore.get()
                        .divide(BigDecimal.valueOf(teacherNumber), 3, BigDecimal.ROUND_HALF_UP)
                        .multiply(teacherFractionRatio)
                        .add(studentScore.get()
                                .divide(BigDecimal.valueOf(studentNumber), 3, BigDecimal.ROUND_HALF_UP)
                                .multiply(studentFractionRatio)));
            }
        }
    }
}
