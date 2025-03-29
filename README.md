# Kafka Distributed Chat
#### Apache Kafka를 이용한 멀티 브로커 환경의 분산 채팅 애플리케이션 [(+ IDP 기술을 적용한 AI 기반의 문서 검색 시스템)](https://github.com/younglee-Choe/kafka-distributed-chat/tree/origin/master/kafka-distributed-chat-python)
<br/>

## 👩🏻‍💻 프로젝트 소개
**Kafka에 채팅 메시지를 분산 저장함으로써 데이터 손실 없이 신뢰성 있는 메시지 전달을 보장하는 채팅 서비스입니다.**

- Kafka에 여러 개의 복제본(Replica)을 두어 데이터를 분산 저장하고 데이터들 간의 동기화를 진행함으로써 3대의 브로커(Kafka 서버) 중 하나가 다운되더라도 시스템의 중단 없이 안정적인 데이터 스트리밍이 가능합니다.
- Fast API와 OpenAI GPT-3.5-turbo 모델을 활용하여 사용자 메시지에 대한 자동 응답 시스템을 추가하고, **IDP(Intelligent Document Processing)** 기술을 적용한 **AI 기반 문서 검색 기능**을 시스템을 구축하여 더욱 효율적인 정보 검색과 처리가 가능하도록 확장하였습니다.
<br/>

## ⚙️ 사용 언어 & 기술
- Languages: `Java 17`, `Python 3.11.4`, `JavaScript`
- Frameworks & Libraries: `Spring Boot 3.1.4`, `FastAPI`, `React 18.2.0`
- Infrastructure & Tools: `Kafka 3.0.0`, `Kafdrop`, `Docker 20.10.17`, `REST API`
- Databases: `MySQL 8.0.41`, `ChromaDB`
- AI & NLP: `OpenAI GPT-3.5-turbo`, `SBERT(Sentence-BERT)`
- Runtime: `Node.js 18`
<br/>

## 💬 데모
### • 3대의 브로커(Kafka 서버) 중 하나가 다운되었을 때 메시지가 정상적으로 Kafka에 저장되는지 확인
<table>
  <tr>
    <td rowspan="2">
      <img width="450" alt="브로커 하나 다운된 상태에서 보내는 메시지" src="https://github.com/user-attachments/assets/ea5f9b3d-4816-440d-b8db-2bebaebdd159" />
        <p>브로커 하나가 다운된 상태에서 보내는 메시지</p>
    </td>
    <td>
      <img width="450" alt="브로커 하나 다운된 상태에서 정상적으로 저장된 메시지1" src="https://github.com/user-attachments/assets/9de228f5-d0ed-4413-9a6a-3f367339d311" />
        <p>브로커 하나가 다운된 상태에서 정상적으로 저장된 메시지1</p>
    </td>
  </tr>
  <tr>
    <td>
      <img width="450" alt="브로커 하나 다운된 상태에서 정상적으로 저장된 메시지2" src="https://github.com/user-attachments/assets/0b99c86b-3398-4c49-a4ab-97e4b61a2490" />
        <p>브로커 하나가 다운된 상태에서 정상적으로 저장된 메시지2</p>
    </td>
  </tr>
</table>
</br>

### • IDP(Intelligent Document Processing) 기술을 적용한 AI 기반 문서 검색 기능
<p>
  <img alt="IDP 1" src="https://github.com/user-attachments/assets/9067f8c4-da38-4955-adc9-b96bb7d9e4f6" />
  <p>[AI를 활용한 질의응답]</p>
  <img alt="IDP 2" src="https://github.com/user-attachments/assets/127e63b7-422b-4d34-ad52-d4dd72e9c556" />
  <p>[AI가 업로드된 문서를 요약 & 분류1]</p>
  <img alt="IDP 3" src="https://github.com/user-attachments/assets/43850ca4-021e-4343-a4c7-9cbc74039799" />
  <p>[AI가 업로드된 문서를 요약 & 분류2]</p>
  <img alt="IDP 2-2" src="https://github.com/user-attachments/assets/a08fd702-21dd-4381-b85e-70de8fcef488" />
  <p>[AI를 활용한 문서에 대한 질의응답]</p>
</p>
</br>

## 🔨 아키텍쳐
<p float="left">
  <img alt="아키텍쳐" src="https://github.com/user-attachments/assets/bfe078aa-e98f-46d5-a664-97f599e6da34" />
  <p>[Architecture (IDP 부분 제외)]</p>
  <img alt="시퀀스 다이어그램" src="https://github.com/user-attachments/assets/9da825cd-921d-4f9a-b429-e44ee3a6eeda" />
  <p>[Sequence Diagram]</p>
  <img alt="ERD" src="https://github.com/user-attachments/assets/41f85fa2-d4f4-42b9-9bc3-2942824544b5">
  <p>[Entity-Relationship Diagram]</p>
</p>
</br>

## 👨‍👩‍👧‍👦 기간 & 인원
* 2023.09 - 2023.11, 2025.03
* **최영리** ([younglee-Choe](https://github.com/younglee-Choe)) 1인 프로젝트
