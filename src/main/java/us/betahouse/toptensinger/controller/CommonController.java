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
 * 通用控制层 可考虑分开
 *
 * @author MessiahJK
 * @version : CommonController.java 2019/03/26 12:18 MessiahJK
 */
@RestController
public class CommonController {

    private final DictionaryUtil dictionaryUtil;
    private final ContestRepository contestRepository;
    private final ScoreRepository scoreRepository;
    private final PlayerRepository playerRepository;

    /**
     * 初始化控制层
     *
     * @param dictionaryUtil 字典工具
     * @param contestRepository 比赛仓库
     * @param scoreRepository 分数仓库
     * @param playerRepository 用户仓库
     */
    @Autowired
    public CommonController(DictionaryUtil dictionaryUtil, ContestRepository contestRepository, ScoreRepository scoreRepository, PlayerRepository playerRepository) {
        this.dictionaryUtil = dictionaryUtil;
        this.contestRepository = contestRepository;
        this.scoreRepository = scoreRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * 获取比赛列表
     *
     * @return 比赛列表
     */
    @CrossOrigin
    @GetMapping("/contest")
    public List<Contest> getContestList(){
        return contestRepository.findAll();
    }

    /**
     * 分数
     *
     * @param id 选手id
     * @param scoreValue 分数
     * @param type 类型
     * @return boolean
     * @throws IOException socket
     */
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

    /**
     * 获取选手列表
     *
     * @return 选手列表
     */
    @CrossOrigin
    @GetMapping("/playerList")
    public List<Player> getPlayerList(){
        Long activeContest=dictionaryUtil.getActiveContest();
        List<Player> playerList=playerRepository.findByContestId(activeContest);
        playerList.forEach(this::complete);
        return  playerList;
    }

    /**
     * 重置选手分数
     *
     * @param id 选手id
     * @return Boolean
     * @throws IOException socket
     */
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

    /**
     * 选择比赛
     *
     * @param id 比赛id
     * @return 比赛列表
     */
    @CrossOrigin
    @PostMapping("/contest")
    public List<Contest> chooseContest(Long id){
        Long activeContest=dictionaryUtil.getActiveContest();
        //修改旧活动
        Contest contest=contestRepository.getOne(activeContest);
        contest.setStatus("sleep");
        contestRepository.save(contest);
        //修改新活动
        Contest newActiveContest=contestRepository.getOne(id);
        newActiveContest.setStatus("active");
        contestRepository.save(newActiveContest);
        //修改字典
        dictionaryUtil.updateActiveContest(newActiveContest.getId(), newActiveContest.getName());
        return getContestList();
    }

    /**
     * 获取选手实体 并广播
     *
     * @param id 选手id
     * @return 选手实体
     * @throws IOException socket
     */
    @CrossOrigin
    @GetMapping("/player")
    public Player getPlayer(Long id) throws IOException {
        Player player=playerRepository.getOne(id);
        complete(player);
        SocketMessage socketMessage=new SocketMessage();
        socketMessage.setName("player");
        socketMessage.add("player", player);
        MyWebSocket.sendInfo(socketMessage.toJSON());
        return player;
    }

    /**
     * 获取分数列表
     *
     * @param id 选手id
     * @return ScoreResult 包含教师分数及学生分数
     */
    @CrossOrigin
    @GetMapping("/score")
    public ScoreResult getScoreResult(Long id){
        Long activeContest=dictionaryUtil.getActiveContest();
        List<Score> studentScoreList,teacherScoreList;
        studentScoreList = scoreRepository.findByContestIdAndPlayerIdAndType(activeContest, id, ScoreType.STUDENT.name());
        teacherScoreList= scoreRepository.findByContestIdAndPlayerIdAndType(activeContest, id, ScoreType.TEACHER.name());
        return new ScoreResult(studentScoreList, teacherScoreList);
    }

    /**
     * 完善player 统计已投人数、平均分
     *
     * @param player 选手实体
     */
    private void complete(Player player) {
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
