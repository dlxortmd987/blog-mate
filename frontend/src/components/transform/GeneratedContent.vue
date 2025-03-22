// components/GeneratedContent.vue - 생성된 블로그 콘텐츠 컴포넌트
<template>
  <div class="h-full flex flex-col">
    <div class="mb-4 flex justify-between items-start">
      <div>
        <h1 class="text-2xl font-semibold text-gray-900">생성된 블로그</h1>
        <p class="text-gray-600">AI가 생성한 블로그 글입니다. 필요에 따라 수정하여 사용하세요.</p>
      </div>
      <div class="flex space-x-2">
        <button
            @click="copyContent"
            class="inline-flex items-center px-3 py-1.5 border border-gray-300 shadow-sm text-sm font-medium rounded text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-orange-500"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 5H6a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2v-1M8 5a2 2 0 002 2h2a2 2 0 002-2M8 5a2 2 0 012-2h2a2 2 0 012 2m0 0h2a2 2 0 012 2v3m2 4H10m0 0l3-3m-3 3l3 3" />
          </svg>
          복사
        </button>
        <button
            @click="exportContent"
            class="inline-flex items-center px-3 py-1.5 border border-gray-300 shadow-sm text-sm font-medium rounded text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-orange-500"
        >
          <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 mr-1" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
          </svg>
          내보내기
        </button>
      </div>
    </div>

    <!-- 추천 제목 선택 -->
    <div class="mb-4">
      <h3 class="text-sm font-medium text-gray-700 mb-2">추천 제목</h3>
      <div class="flex flex-wrap gap-2">
        <button
            v-for="(title, index) in titles"
            :key="index"
            @click="selectTitle(title)"
            :class="[
            'px-3 py-1.5 text-sm rounded-md transition-colors border',
            selectedTitle === title
              ? 'bg-orange-100 text-orange-700 border-orange-300'
              : 'bg-white text-gray-700 border-gray-300 hover:bg-gray-50'
          ]"
        >
          {{ title }}
        </button>
      </div>
    </div>

    <!-- 키워드 -->
    <div class="mb-4">
      <h3 class="text-sm font-medium text-gray-700 mb-2">키워드</h3>
      <div class="flex flex-wrap gap-1">
        <span
            v-for="(keyword, index) in keywords"
            :key="index"
            class="px-2 py-1 text-xs rounded-full bg-gray-100 text-gray-700"
        >
          #{{ keyword }}
        </span>
      </div>
    </div>

    <!-- 생성된 콘텐츠 표시 -->
    <div class="flex-1 border border-gray-200 rounded-lg p-4 overflow-auto">
      <div v-if="selectedTitle" class="text-2xl font-bold mb-4">{{ selectedTitle }}</div>

      <!-- 콘텐츠를 HTML로 렌더링 -->
      <div class="prose prose-orange max-w-none" v-html="formattedContent"></div>
    </div>

    <!-- 토스트 메시지 -->
    <div
        v-if="showToast"
        class="fixed bottom-4 left-1/2 transform -translate-x-1/2 bg-gray-800 text-white px-4 py-2 rounded-md shadow-lg"
    >
      {{ toastMessage }}
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue';

export default {
  props: {
    content: {
      type: String,
      required: true
    },
    titles: {
      type: Array,
      default: () => []
    },
    keywords: {
      type: Array,
      default: () => []
    }
  },

  setup(props) {
    const selectedTitle = ref('');
    const showToast = ref(false);
    const toastMessage = ref('');

    // 선택된 제목이 없을 때 첫 번째 제목을 기본값으로 사용
    onMounted(() => {
      if (props.titles && props.titles.length > 0) {
        selectedTitle.value = props.titles[0];
      }
    });

    // 줄바꿈, 리스트 등 기본 마크업 처리
    const formattedContent = computed(() => {
      if (!props.content) return '';

      // 이미 HTML인 경우 그대로 반환
      if (props.content.includes('<p>') || props.content.includes('<h')) {
        return props.content;
      }

      // 간단한 마크다운 스타일 변환
      let formatted = props.content
          // 헤더
          .replace(/^# (.+)$/gm, '<h1>$1</h1>')
          .replace(/^## (.+)$/gm, '<h2>$1</h2>')
          .replace(/^### (.+)$/gm, '<h3>$1</h3>')
          // 줄바꿈을 <p> 태그로 변환
          .split('\n\n')
          .map(paragraph => `<p>${paragraph}</p>`)
          .join('')
          // 리스트 아이템
          .replace(/<p>- (.+)<\/p>/g, '<li>$1</li>')
          // 리스트 아이템을 감싸는 ul 태그 추가
          .replace(/(<li>.+<\/li>\s*)+/g, '<ul>$&</ul>');

      return formatted;
    });

    // 제목 선택 함수
    function selectTitle(title) {
      selectedTitle.value = title;
    }

    // 콘텐츠 복사 함수
    function copyContent() {
      const textToCopy = `${selectedTitle.value}\n\n${props.content}`;

      navigator.clipboard.writeText(textToCopy)
          .then(() => {
            showToastMessage('클립보드에 복사되었습니다.');
          })
          .catch(() => {
            showToastMessage('복사에 실패했습니다.');
          });
    }

    // 내보내기 함수
    function exportContent() {
      const textToExport = `${selectedTitle.value}\n\n${props.content}`;
      const blob = new Blob([textToExport], { type: 'text/plain' });
      const url = URL.createObjectURL(blob);

      const a = document.createElement('a');
      a.href = url;
      a.download = `${selectedTitle.value || '블로그포스트'}.txt`;
      document.body.appendChild(a);
      a.click();

      setTimeout(() => {
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
        showToastMessage('파일이 다운로드되었습니다.');
      }, 100);
    }

    // 토스트 메시지 표시 함수
    function showToastMessage(message) {
      toastMessage.value = message;
      showToast.value = true;

      setTimeout(() => {
        showToast.value = false;
      }, 3000);
    }

    return {
      selectedTitle,
      formattedContent,
      showToast,
      toastMessage,
      selectTitle,
      copyContent,
      exportContent
    };
  }
};
</script>

<style scoped>
/* Prose 텍스트 스타일링 */
.prose {
  color: #374151;
  max-width: 65ch;
  font-size: 1rem;
  line-height: 1.75;
}

.prose h1,
.prose h2,
.prose h3 {
  color: #111827;
  margin-top: 1.5em;
  margin-bottom: 0.5em;
  font-weight: 600;
}

.prose h1 {
  font-size: 1.875em;
  line-height: 1.1;
  margin-top: 0;
}

.prose h2 {
  font-size: 1.5em;
  line-height: 1.3;
}

.prose h3 {
  font-size: 1.25em;
  line-height: 1.4;
}

.prose p {
  margin-top: 1.25em;
  margin-bottom: 1.25em;
}

.prose ul {
  margin-top: 1.25em;
  margin-bottom: 1.25em;
  padding-left: 1.625em;
}

.prose li {
  margin-top: 0.5em;
  margin-bottom: 0.5em;
}

.prose strong {
  font-weight: 600;
  color: #111827;
}

.prose-orange a {
  color: #ea580c;
  text-decoration: underline;
  font-weight: 500;
}
</style>