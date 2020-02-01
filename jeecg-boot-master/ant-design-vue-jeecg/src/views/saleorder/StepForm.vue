<template>
  <a-card :bordered="false">
    <a-steps class="steps" :current="currentTab">
      <a-step title="录入订单信息" />
      <a-step title="确认及收款" />
      <a-step title="发货" />
      <a-step title="完成" />
    </a-steps>
    <div class="content">
      <step1 v-if="currentTab === 0" @nextStep="nextStep"/>
      <step2 v-if="currentTab === 1" @nextStep="nextStep" @prevStep="prevStep"/>
      <step3 v-if="currentTab === 2" @prevStep="prevStep" @nextStep="nextStep"/>
      <step4 v-if="currentTab === 3" @prevStep="prevStep" @finish="finish"/>
    </div>
  </a-card>
</template>

<script>
  import Step1 from './Step1'
  import Step2 from './Step2'
  import Step3 from './Step3'
  import Step4 from './Step4'

  export default {
    name: "StepForm",
    components: {
      Step1,
      Step2,
      Step3,
      Step4
    },
    data () {
      return {
        description: '将一个冗长或用户不熟悉的表单任务分成多个步骤，指导用户完成。',
        currentTab: 0,

        // form
        form: null,
      }
    },
    methods: {

      // handler
      nextStep () {
        if (this.currentTab < 3) {
          this.currentTab += 1
        }
      },
      prevStep () {
        if (this.currentTab > 0) {
          this.currentTab -= 1
        }
      },
      finish () {
        this.$route.query.unEditable = true
        this.currentTab = 0
        this.$parent.closeRouteViewTab(this.$route.fullPath)
      }
    }
  }
</script>

<style lang="scss" scoped>
  .steps {
    max-width: 750px;
    margin: 16px auto;
  }
</style>