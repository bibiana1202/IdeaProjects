package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerContentAndCreateDate;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;
import com.mysite.sbb.question.QuestionSubjectAndContent;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionService questionService;

	@Disabled("Qusetion 테이블의 샘플 데이터 2건 생성")
	@Test
	void testQuestionCreateData() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);  // 첫번째 질문 저장

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);  // 두번째 질문 저장
	}

	@Test
	void testQuestionFindAll(){
		List<Question> all = this.questionRepository.findAll();
		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	@Test
	void testQuestionFindById() {
		Optional<Question> oq = this.questionRepository.findById(1);
		if(oq.isPresent()) {
			Question q = oq.get();
			assertEquals("sbb가 무엇인가요?", q.getSubject());
		}
	}
	@Test
	void testQuestionFindBySubject() {
		Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
		assertEquals(1, q.getId());
		log.info(q.getContent());
	}

	@Test
	void testQuestionFindBySubjectAndContent() {
		Question q = this.questionRepository.findBySubjectAndContent(
				"sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertEquals(1, q.getId());
	}

	@Test
	void testQuestionFindBySubjectLike(){
		List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
		Question q = qList.get(0);
		assertEquals("sbb가 무엇인가요?",q.getSubject());
	}

	@Test
	void testQuestionUpdate() {
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");
		this.questionRepository.save(q);
	}

	@Test
	void testQuestionDelete() {
		assertEquals(2, this.questionRepository.count());
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);
		assertEquals(1, this.questionRepository.count());
	}

	@Disabled
	@Test
	void testAnswerCreateData() {
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		Answer a = new Answer();
		a.setContent("네 자동으로 생성될까용?.");
		a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);
	}

	// 답변 데이터를 통해 질문 데이터 찾기
	@Test
	void testAnswerFindById() {
		Optional<Answer> oa = this.answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());
	}


	// 질문 데이터를 통해 답변 데이터 찾기
	@Test
	@Transactional // 메서드가 종료될 때 까지 DB 세션이 유지된다.
	// 질문 하나의 답변 여러개 일수있는 일대다 관계이므로
	void testQuestionFindById2() {
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		// ONETOMANY 지연로딩
		List<Answer> answerList = q.getAnswerList();

		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
	}


	@Test
	void testFindSubAndConById(){
		Optional<QuestionSubjectAndContent> oq = this.questionRepository.findSubAndConById(2);
		if(oq.isPresent()) {
			String subject = oq.get().getSubject();
			String content = oq.get().getContent();
			assertEquals("스프링부트 모델 질문입니다.",subject);
			assertEquals("id는 자동으로 생성되나요?",content);
			log.info(subject);
			log.info(content);
		}
	}

	@Test // answer 의 content 와 create_date 가져오기
	void testFindAnswerAndCreateDateById(){
		Optional<AnswerContentAndCreateDate> oa = this.answerRepository.findContentAndCreateDateById(1);
		if(oa.isPresent()) {
			String content = oa.get().getContent();
			LocalDateTime createDate = oa.get().getCreateDate();
			assertEquals("네 자동으로 생성됩니다.",content);
			log.info(createDate);
			log.info(content);
		}
	}

	@Test
	void testCreateTestCase(){
		for(int i = 1 ; i <= 300; i ++){
			String subject = String.format("테스트 데이터입니다:[%03d]",i);
			String content = "내용무";
			this.questionService.create(subject,content,null);
		}
	}
}
