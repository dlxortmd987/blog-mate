// services/transformService.js - API 통신 서비스
import axios from 'axios';

// API 기본 설정
const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    },
    timeout: 30000 // 30초 타임아웃 (AI 변환에 시간이 걸릴 수 있음)
});

// 에러 처리 함수
const handleApiError = (error) => {
    // 응답이 있는 경우
    if (error.response) {
        console.error('API Error Response:', error.response.data);
        // 서버 에러 메시지가 있으면 그것을 반환, 없으면 기본 메시지
        const message = error.response.data?.message || '서버 오류가 발생했습니다.';
        throw new Error(message);
    }
    // 요청은 보냈지만 응답을 받지 못한 경우
    else if (error.request) {
        console.error('API No Response:', error.request);
        throw new Error('서버 응답이 없습니다. 네트워크 연결을 확인해주세요.');
    }
    // 그 외 에러
    else {
        console.error('API Error:', error.message);
        throw new Error('요청 준비 중 오류가 발생했습니다.');
    }
};

export default {
    /**
     * 초안을 블로그 콘텐츠로 변환
     * @param {Object} contentRequest - 변환 요청 객체
     * @param {string} contentRequest.contentLength - 콘텐츠 길이 (MIN, MEDIUM, MAX)
     * @param {string} contentRequest.contentType - 콘텐츠 유형 (PROGRAMMING, FOOD, ...)
     * @param {string} contentRequest.draft - 사용자가 입력한 초안 내용
     * @returns {Promise<Object>} - 변환 결과 객체 (content, titles, keywords)
     */
    async transformContent(contentRequest) {
        try {
            // 1. 실제 API 호출
            const response = await apiClient.post('/blog/transform', contentRequest);
            return response.data;

            // 백엔드 API 개발 전 테스트용 더미 응답
            /*
            // 요청에 따라 응답 시간 조정 (실제 API 호출 시뮬레이션)
            const responseTime = Math.floor(Math.random() * 1000) + 1000; // 1-2초
            await new Promise(resolve => setTimeout(resolve, responseTime));

            // 더미 응답 데이터
            const dummyResponse = {
              titles: [
                "도커 컨테이너 기초 이해하기: 가상화의 새로운 패러다임",
                "도커 입문: 컨테이너 기술의 모든 것",
                "개발자를 위한 도커 컨테이너 완벽 가이드"
              ],
              content: `# 도커 컨테이너 기초 이해하기

      ## 1. 도커란 무엇인가?

      도커(Docker)는 애플리케이션을 개발, 배포, 실행하기 위한 오픈 소스 플랫폼입니다. 도커를 사용하면 애플리케이션을 인프라에서 분리하여 소프트웨어를 빠르게 제공할 수 있습니다. 도커를 사용하면 코드를 관리하는 것과 동일한 방식으로 인프라를 관리할 수 있습니다.

      도커는 컨테이너라는 격리된 환경에서 애플리케이션을 패키징하고 실행할 수 있게 해주는 도구입니다. 이를 통해 개발자는 "내 컴퓨터에서는 작동했는데..."라는 문제를 해결할 수 있습니다.

      ## 2. 컨테이너와 VM의 차이점

      가상 머신(VM)은 하이퍼바이저를 통해 여러 운영 체제를 실행하는 반면, 컨테이너는 동일한 운영 체제 커널을 공유하면서 프로세스를 격리된 환경에서 실행합니다.

      주요 차이점:

      - **리소스 효율성**: 컨테이너는 호스트 OS 커널을 공유하므로 VM보다 훨씬 가볍습니다.
      - **시작 시간**: 컨테이너는 초 단위로 시작되는 반면 VM은 분 단위가 걸릴 수 있습니다.
      - **격리 수준**: VM은 하드웨어 수준에서 완전히 격리되는 반면, 컨테이너는 프로세스 수준에서 격리됩니다.

      ## 3. 기본 명령어

      도커를 사용하기 위한 기본 명령어는 다음과 같습니다:

      - **docker run**: 이미지로부터 컨테이너를 생성하고 시작합니다.
        예: \`docker run -d -p 80:80 nginx\`

      - **docker ps**: 실행 중인 컨테이너 목록을 보여줍니다.
        예: \`docker ps\` 또는 모든 컨테이너를 보려면 \`docker ps -a\`

      - **docker stop**: 실행 중인 컨테이너를 중지합니다.
        예: \`docker stop container_id\`

      - **docker build**: Dockerfile로부터 이미지를 빌드합니다.
        예: \`docker build -t my-app:1.0 .\`

      ## 4. 실제 사용 사례

      도커는 다양한 환경에서 활용됩니다:

      ### 개발 환경
      개발자는 로컬 머신에서 프로덕션 환경과 동일한 환경을 구성할 수 있습니다. 이를 통해 "내 PC에서는 작동하는데..."라는 문제를 해결할 수 있습니다.

      ### CI/CD 파이프라인
      도커는 지속적 통합 및 배포 파이프라인에서 빌드, 테스트, 배포 프로세스를 일관되게 유지하는 데 도움이 됩니다.

      ### 마이크로서비스 아키텍처
      각 서비스를 독립적인 컨테이너로 실행함으로써 확장성과 유지보수성을 향상시킬 수 있습니다.

      도커는 현대 소프트웨어 개발 및 운영에 혁명을 가져왔으며, 클라우드 네이티브 애플리케이션 개발의 핵심 요소가 되었습니다.`,
              keywords: [
                "도커", "컨테이너", "가상화", "Docker", "DevOps", "클라우드", "마이크로서비스"
              ]
            };

            return dummyResponse;
            */
        } catch (error) {
            return handleApiError(error);
        }
    }
};