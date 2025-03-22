// App.vue - 메인 애플리케이션 컴포넌트
<template>
  <div class="min-h-screen bg-gray-50 flex flex-col">
    <!-- 헤더 -->
    <header class="bg-white shadow-sm">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16 items-center">
          <div class="flex items-center">
            <div class="flex-shrink-0">
              <h1 class="text-xl font-semibold text-gray-900">BlogMate</h1>
            </div>
          </div>
          <div class="flex items-center">
            <button class="ml-2 p-2 rounded-full text-gray-500 hover:text-gray-700 focus:outline-none">
              <span class="sr-only">설정</span>
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
            </button>
            <button class="ml-2 p-2 rounded-full text-gray-500 hover:text-gray-700 focus:outline-none">
              <span class="sr-only">계정</span>
              <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- 메인 컨텐츠 -->
    <main class="flex-1 flex flex-col lg:flex-row">
      <!-- 초안 작성 영역 -->
      <div class="flex-1 bg-white p-4 sm:p-6 lg:border-r-2 lg:border-gray-100 overflow-auto">
        <DraftEditor
            v-model="draftContent"
            @transform="handleTransform"
            :isLoading="isLoading"
        />
      </div>

      <!-- 생성된 콘텐츠 영역 -->
      <div
          class="flex-1 bg-white p-4 sm:p-6 overflow-auto"
          :class="{'hidden lg:block': !showGeneratedContent && !generatedContent}"
      >
        <template v-if="generatedContent">
          <GeneratedContent
              :content="generatedContent"
              :titles="generatedTitles"
              :keywords="generatedKeywords"
          />
        </template>
        <div v-else-if="isLoading" class="h-full flex flex-col items-center justify-center">
          <div class="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-orange-500"></div>
          <p class="mt-4 text-gray-600">블로그 글을 생성하는 중...</p>
        </div>
        <div v-else class="h-full flex flex-col items-center justify-center text-gray-400">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
          </svg>
          <p class="text-lg">왼쪽의 초안 에디터에 내용을 입력하고 변환 버튼을 클릭해보세요.</p>
        </div>
      </div>

      <!-- 모바일 토글 버튼 -->
      <div class="fixed bottom-4 right-4 lg:hidden" v-if="generatedContent">
        <button
            @click="toggleView"
            class="bg-orange-500 text-white rounded-full p-4 shadow-lg"
        >
          <svg v-if="!showGeneratedContent" xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
          <svg v-else xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
          </svg>
        </button>
      </div>
    </main>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import DraftEditor from './components/editor/DraftEditor.vue';
import GeneratedContent from './components/transform/GeneratedContent.vue';
import transformService from './services/transformService';

export default {
  components: {
    DraftEditor,
    GeneratedContent
  },
  setup() {
    const draftContent = ref('');
    const generatedContent = ref('');
    const generatedTitles = ref([]);
    const generatedKeywords = ref([]);
    const isLoading = ref(false);
    const showGeneratedContent = ref(false);

    // 모바일에서 초안/결과 토글
    function toggleView() {
      showGeneratedContent.value = !showGeneratedContent.value;

      // 모바일에서 결과를 보여줄 때 초안 에디터 숨기기
      if (showGeneratedContent.value) {
        document.querySelector('main > div:first-child').classList.add('hidden');
        document.querySelector('main > div:nth-child(2)').classList.remove('hidden');
      } else {
        document.querySelector('main > div:first-child').classList.remove('hidden');
        document.querySelector('main > div:nth-child(2)').classList.add('hidden');
      }
    }

    async function handleTransform(options) {
      try {
        isLoading.value = true;

        // 모바일에서 결과 영역으로 자동 전환
        if (window.innerWidth < 1024) {
          showGeneratedContent.value = true;
          document.querySelector('main > div:first-child').classList.add('hidden');
          document.querySelector('main > div:nth-child(2)').classList.remove('hidden');
        }

        const result = await transformService.transformContent({
          contentLength: options.contentLength,
          contentType: options.contentType,
          draft: draftContent.value
        });

        generatedContent.value = result.content;
        generatedTitles.value = result.titles;
        generatedKeywords.value = result.keywords;
      } catch (error) {
        console.error('변환 중 오류 발생:', error);
        alert('블로그 글 생성 중 오류가 발생했습니다. 다시 시도해주세요.');
      } finally {
        isLoading.value = false;
      }
    }

    // 로컬 스토리지에서 초안 불러오기
    onMounted(() => {
      const savedDraft = localStorage.getItem('blogmate_draft');
      if (savedDraft) {
        draftContent.value = savedDraft;
      }
    });

    return {
      draftContent,
      generatedContent,
      generatedTitles,
      generatedKeywords,
      isLoading,
      showGeneratedContent,
      handleTransform,
      toggleView
    };
  }
};
</script>

<style>
@media (max-width: 1023px) {
  main > div:nth-child(2) {
    display: none;
  }
}
</style>