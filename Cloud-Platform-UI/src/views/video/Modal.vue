<template>
  <div class="modal-bg" v-show="show">
    <div class="modal-container">
      <div class="modal-header">
        {{title}}
      </div>
      <div class="modal-main">
        <slot>
        </slot>
      </div>
      <div class="modal-footer">
        <button @click="hidenModal">取 消</button>
        <button @click="submit">确 定</button>
      </div>
    </div>
  </div>
</template>

<script>
    export default {
      name: 'modal',
      props: {
        show: {
          type: Boolean,
          default: false
        },
        title: {
          type: String,
          default: ''
        }
      },
      methods: {
        hidenModal() {
          this.$emit('hidenModal')
        },
        submit() {
          this.$emit('submit')
        }
      }
    }
</script>

<style scoped>

</style>
