# 📰 2024_Capstone_Design AI based on News application
### AI기반 맞춤형 뉴스 어플리케이션
<br>

## 요약
최근 스마트폰과 같은 모바일 장치의 보급이 늘어남에 따라 사용하는 인구도 역시 비례하여 증가하고 있다.<br>
또한 빠르게 변화하는 현대 사회에서 정확한 정보를 지속적으로 얻는 것은 필수이며,<br>
사용자가 온라인에서 접할 수 있는 데이터의 양은 방대하고 신뢰성 있는 뉴스를 찾기에는 다소 어려울 수 있다.<br>
AI를 활용하여 사용자가 편리하게 접근할 수 있는 뉴스 애플리케이션을 개발하고자 한다.<br><br>



## 서론
현대 디지털 시대의 빠른 정보 확산 속도와 사회적 변화는 정확하고 신뢰할 수 있는 정보에 대한 지속적인 접근을 요구하고 있다.<br>
최근 조사에 따르면 한국 성인 스마트폰 사용률이 상당한 것으로 나타났고, 미디어 이용률 또한 스마트폰 사용률에 비례하여 증가하였다.<br>
이는 정보를 얻기 위해 모바일 기술에 대한 의존도가 높아지고 있음을 반영한다.<br>
그러나 온라인에서 접하거나 사용할 수 있는 데이터의 양은 압도적이기 때문에 정보를 식별하는 것이 점점 더 어려워지고 있다.<br><br>

그래서 우리는 사용자가 양질의 뉴스 콘텐츠에 대한 접근을 간소화하도록 설계된 AI 기반 맞춤형 뉴스 애플리케이션을 제안한다<br>
이 애플리케이션에는 자연어 처리(NLP) 기능이 탑재되어 있어 뉴스 기사를 효과적으로 요약하고 간결하고 관련성 있는 정보를 쉽게 사용할 수 있는 형식으로 제공할 수 있다.<br><br>

본 연구에서는 단순성과 사용 용이성을 강조하면서 사용자가 최신 정보를 얻을 수 있는 편리하고 접근 가능한 수단을 제공하는 애플리케이션을 개발한다.<br><br>

## 개론
20대와 30대의 전통 미디어 이탈과 밀레니얼 세대를 겨냥한 뉴스 미디어 창간 러시 등 뉴스 이용에서 세대의 특성에 주목할 필요성이 제기된다.<br>
모바일과 SNS를 통한 뉴스 이용으로 저널리즘 환경이 급격히 변화하고 있다.<br>
그러한 변화의 중심에‘밀레니얼 세대’(Millennial generation)로 불리는 20, 30대가 있다.<br><br>

![image](https://github.com/user-attachments/assets/cdeb746c-3d89-4f2c-9463-e52f04de3a8f)
![image](https://github.com/user-attachments/assets/1c89f0ed-acf5-4550-94a7-370d299a22cf)<br>
**스마트폰 사용률 2012-2024, 매체별 뉴스 이용률 추이**<br><br>


## 설계
<p align = "center"><img src = https://github.com/user-attachments/assets/c7eb4bf9-109c-4a05-b82b-1ffce964a923 width = "500" height = "100%"></p>

 ### 시스템 구조도
<br><br><br>

<p align = "center"><img src = https://github.com/user-attachments/assets/301f5cee-bf15-4216-a19b-5d48280da42f width = "500" hegith = "100%"></p>

### 기술 프레임워크<br><br>
개발언어 및 도구는 Kotlin과 Android Studio, 서버와의 네트워크 통신을 위해 retrofit 라이브러리를 사용했으며 외부 API는 Newsdata,io를 사용한다.<br>
또한 JSON 응답을 Kotlin 객체로 변환하기 위해 gson 라이브러리를, 데이터베이스는 Room 라이브러리를 이용하여 내부 SQL Database인 SQLite Database를 사용한다.<br>
요약과 추천 기능을 위해 OpenAI의 GPT-3.5 모델을 통합하여 구현하였으며, 이를 위해 세분화된 HTTP 구성, 사용자 정의 헤더 또는 복잡한 JSON 페이로드 처리가 가능한 OkHttp 라이브러리를 사용하여 API와의 HTTP 통신을 처리한다.<br>
비동기 작업 관리를 위해 코루틴을 사용하며, JSON 객체 및 배열을 활용하여 요청 데이터를 구성하고 응답 데이터를 파싱한다.<br>
<br><br><br><br><br><br>

<p align = "center"><img src = https://github.com/user-attachments/assets/73d81caa-2d7c-48b1-9a81-36b42515710a width = "500" hegith = "100%"></p>



### DB 구성
<br><br>
각 테이블에는 데이터 접근을 위한 DAO(Data Access Object)를 설계하였으며, 이를 통해 데이터를 삽입, 삭제, 조회할 수 있는 기능을 제공한다.<br>
본 애플리케이션은 Newsdata.io API에서 뉴스 데이터를 검색하고 수정하도록 설계되었다.<br>
네트워크 통신을 최적화하기 위해 앱 전체에서 공유 인스턴스가 사용되어 메모리 사용량을 최소화하고 코드 일관성을 보장한다.<br>
데이터 저장 및 변환이 효율적으로 처리되므로 복잡한 개체를 데이터베이스에 저장하고 필요에 따라 재구성할 수 있다.<br>
MVVM 아키텍처 패턴은 UI에서 데이터를 분리하는 데 사용되며, 데이터가 변경될 때마다 실시간 업데이트가 가능하여 사용자에게 최신 정보를 제공한다.<br><br>

<p align = "center"><img src = https://github.com/user-attachments/assets/3b71c2b4-a327-424d-9929-89adee8b4bac width = "500", hegith = "100%"></p>
<br><br>

### Nav로 연결된 여러 Fragment <br><br>
애플리케이션의 구조이며 여러 화면 간의 이동 경로를 가시화하여 시각적으로 명시한다.<br>
사용자는 하단의 버튼을 통해 원하는 화면으로 쉽게 전환할 수 있다.<br>
HeadlineFragment은 사용자가 처음으로 보는 메인화면으로, 이곳에서 사용자는 원하는 뉴스를 클릭 시 NewPageFragment로 이동되어 사용자는 뉴스 화면을 볼 수 있고<br>
FavouritesFragment를 통해 사용자가 저장한 즐겨찾기 목록을 확인할 수 있으며<br>
SearchFragment를 통해 사용자는 원하는 뉴스 기사를 찾을 수 있고 SettingFragment를 통해 사용자가 환경설정을 할 수 있다.<br><br>

