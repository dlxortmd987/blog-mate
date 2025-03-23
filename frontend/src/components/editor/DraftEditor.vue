// components/DraftEditor.vue - 초안 작성 에디터 컴포넌트
<template>
  <div class="h-full flex flex-col">
    <div class="mb-4">
      <h1 class="text-2xl font-semibold text-gray-900">초안 작성</h1>
      <p class="text-gray-600">메모, 개요 또는 아이디어를 입력하면 AI가 완성된 블로그 글로 변환해 드립니다.</p>
    </div>

    <!-- 에디터 영역 -->
    <div class="flex-1 border border-gray-200 rounded-lg overflow-hidden mb-4">
      <div class="border-b border-gray-200 bg-gray-50 px-4 py-2 flex space-x-2">
        <button
            v-for="(tool, index) in editorTools"
            :key="index"
            class="p-1 text-gray-500 hover:text-gray-800 focus:outline-none"
            @click="executeCommand(tool.command, tool.value)"
        >
          <component :is="tool.icon" class="h-5 w-5" />
        </button>
      </div>
      <div
          class="p-4 h-full min-h-[300px] focus:outline-none"
          contenteditable="true"
          ref="editor"
          @input="updateContent"
          @blur="saveDraft"
          placeholder="여기에 메모, 개요 또는 아이디어를 입력하세요..."
      ></div>
    </div>

    <!-- 변환 옵션 영역 -->
    <div class="bg-gray-50 rounded-lg p-4">
      <div class="mb-4">
        <h3 class="text-sm font-medium text-gray-700 mb-2">블로그 스타일</h3>
        <div class="flex flex-wrap gap-2">
          <button
              v-for="style in styles"
              :key="style.value"
              @click="selectedStyle = style.value"
              :class="[
              'px-3 py-1.5 text-sm rounded-md transition-colors',
              selectedStyle === style.value
                ? 'bg-orange-100 text-orange-700 border border-orange-300'
                : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-50'
            ]"
          >
            {{ style.label }}
          </button>
        </div>
      </div>

      <div class="grid grid-cols-2 gap-4">
        <div>
          <h3 class="text-sm font-medium text-gray-700 mb-2">콘텐츠 길이</h3>
          <select
              v-model="selectedLength"
              class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-orange-500 focus:border-orange-500"
          >
            <option
                v-for="length in lengths"
                :key="length.value"
                :value="length.value"
            >
              {{ length.label }}
            </option>
          </select>
        </div>

        <div>
          <h3 class="text-sm font-medium text-gray-700 mb-2">콘텐츠 유형</h3>
          <select
              v-model="selectedType"
              class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-orange-500 focus:border-orange-500"
          >
            <option
                v-for="type in contentTypes"
                :key="type.value"
                :value="type.value"
            >
              {{ type.label }}
            </option>
          </select>
        </div>
      </div>

      <button
          @click="transform"
          :disabled="isLoading || !hasContent"
          class="mt-4 w-full py-3 px-4 rounded-md text-white font-medium transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-orange-500"
          :class="(isLoading || !hasContent) ? 'bg-orange-300 cursor-not-allowed' : 'bg-orange-500 hover:bg-orange-600'"
      >
        <span v-if="isLoading" class="flex items-center justify-center">
          <svg class="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          변환 중...
        </span>
        <span v-else>
          블로그 글로 변환하기
        </span>
      </button>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue';

export default {
  props: {
    modelValue: {
      type: String,
      default: ''
    },
    isLoading: {
      type: Boolean,
      default: false
    }
  },

  emits: ['update:modelValue', 'transform'],

  setup(props, { emit }) {
    const editor = ref('');
    const content = ref(props.modelValue);

    const styles = [
      { value: 'PROFESSIONAL', label: '전문적인' },
      { value: 'CASUAL', label: '친근한' },
      { value: 'INFORMATIVE', label: '정보적인' },
      { value: 'PERSUASIVE', label: '설득력 있는' }
    ];

    const lengths = [
      { value: 'MIN', label: '짧게 (500-1000자)' },
      { value: 'MEDIUM', label: '중간 (1000-2000자)' },
      { value: 'MAX', label: '길게 (2000-3000자)' }
    ];

    const contentTypes = [
      { value: 'PROGRAMMING', label: '프로그래밍/기술' },
      { value: 'FOOD', label: '음식/요리' },
      // { value: 'TRAVEL', label: '여행' },
      // { value: 'LIFESTYLE', label: '라이프스타일' },
      { value: 'NONE', label: '일반' }
    ];

    const editorTools = [
      {
        command: 'bold',
        icon: {
          template: `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M6 4h8a4 4 0 0 1 4 4 4 4 0 0 1-4 4H6z"></path><path d="M6 12h9a4 4 0 0 1 4 4 4 4 0 0 1-4 4H6z"></path></svg>`
        }
      },
      {
        command: 'italic',
        icon: {
          template: `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="4" x2="10" y2="4"></line><line x1="14" y1="20" x2="5" y2="20"></line><line x1="15" y1="4" x2="9" y2="20"></line></svg>`
        }
      },
      {
        command: 'insertUnorderedList',
        icon: {
          template: `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="8" y1="6" x2="21" y2="6"></line><line x1="8" y1="12" x2="21" y2="12"></line><line x1="8" y1="18" x2="21" y2="18"></line><line x1="3" y1="6" x2="3.01" y2="6"></line><line x1="3" y1="12" x2="3.01" y2="12"></line><line x1="3" y1="18" x2="3.01" y2="18"></line></svg>`
        }
      },
      {
        command: 'insertOrderedList',
        icon: {
          template: `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="10" y1="6" x2="21" y2="6"></line><line x1="10" y1="12" x2="21" y2="12"></line><line x1="10" y1="18" x2="21" y2="18"></line><path d="M4 6h1v4"></path><path d="M4 10h2"></path><path d="M6 18H4c0-1 2-2 2-3s-1-1.5-2-1"></path></svg>`
        }
      },
      {
        command: 'formatBlock',
        value: '<h2>',
        icon: {
          template: `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 12h8"></path><path d="M4 18V6"></path><path d="M12 18V6"></path><path d="M17.5 10.5c.5-1 1.5-2 2.5-2 2 0 3 1.5 3 3s-1 3-3 3c-1 0-2-1-2.5-2"></path></svg>`
        }
      }
    ];

    const selectedStyle = ref('PROFESSIONAL');
    const selectedLength = ref('MEDIUM');
    const selectedType = ref('NONE');

    // 콘텐츠가 있는지 확인
    const hasContent = computed(() => {
      return content.value.trim().length > 3;
    });

    // 에디터 명령 실행 함수
    function executeCommand(command, value) {
      document.execCommand(command, false, value);
      editor.value.focus();
      updateContent();
    }

    // 콘텐츠 업데이트 함수
    function updateContent() {
      if (editor.value) {
        content.value = editor.value.innerHTML;
        emit('update:modelValue', content.value);
      }
    }

    // 로컬 스토리지에 저장
    function saveDraft() {
      if (editor.value) {
        localStorage.setItem('blogmate_draft', editor.value.innerHTML);
      }
    }

    // 변환 함수
    function transform() {
      if (!hasContent.value || props.isLoading) return;

      emit('transform', {
        style: selectedStyle.value,
        contentLength: selectedLength.value,
        contentType: selectedType.value
      });
    }

    // 초기 콘텐츠 설정
    onMounted(() => {
      if (editor.value && props.modelValue) {
        editor.value.innerHTML = props.modelValue;
        content.value = props.modelValue;
      }
    });

    // modelValue가 변경되면 에디터 내용 업데이트
    watch(() => props.modelValue, (newValue) => {
      if (editor.value && newValue !== editor.value.innerHTML) {
        editor.value.innerHTML = newValue;
        content.value = newValue;
      }
    });

    return {
      editor,
      styles,
      lengths,
      contentTypes,
      editorTools,
      selectedStyle,
      selectedLength,
      selectedType,
      hasContent,
      executeCommand,
      updateContent,
      saveDraft,
      transform
    };
  }
};
</script>

<style scoped>
/* 에디터 커스텀 스타일 */
[contenteditable="true"]:empty:before {
  content: attr(placeholder);
  color: #9ca3af;
  cursor: text;
  pointer-events: none;
}

[contenteditable="true"] {
  min-height: 200px;
  height: 100%;
  overflow-y: auto;
}
</style>