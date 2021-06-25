import Login from '@/views/Login';
import Home from '@/components/Layout/Home.vue';

export default [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/home',
    component: Home,
    redirect: '/welcome',
    children: [
      {
        path: '/welcome',
        component: () => import('@/views/Welcome.vue')
      },
      {
        path: '/commerce-article',
        component: () => import('@/views/Commerce/AdminArticle.vue')
      },
      {
        path: '/mark-condition',
        component: () => import('@/views/Commerce/MarketCondition.vue')
      },
      {
        path: '/productioin-marketing-information',
        component: () => import('@/views/Commerce/ProductionMarketingInformation.vue')
      },
      {
        path: '/high-quality-product',
        component: () => import('@/views/Commerce/HighQualityProduct.vue')
      },
      {
        path: '/price-quotation',
        component: () => import('@/views/Commerce/PriceQuotation.vue')
      },
      {
        path: '/message-board',
        component: () => import('@/views/Commerce/MessageBoard.vue')
      },
      {
        path: '/commerce-admin-swiper',
        component: () => import('@/views/Commerce/CommerceAdminSwiper.vue')
      },
      {
        path: '/data-dictionary',
        component: () => import('@/views/Commerce/DataDictionary.vue')
      },
      {
        path: '/admin-article',
        component: () => import('@/views/PublicWelfare/AdminArticle.vue')
      },
      {
        path: '/trade-leads',
        component: () => import('@/views/PublicWelfare/TradeLeads.vue')
      },
      {
        path: '/expert-service',
        component: () => import('@/views/PublicWelfare/ExpertService.vue')
      },
      {
        path: '/farming-question-answer',
        component: () => import('@/views/PublicWelfare/FarmingQA.vue')
      },
      {
        path: '/farming-admin-video',
        component: () => import('@/views/PublicWelfare/FarmingAdminVideo.vue')
      },
      {
        path: '/public-welfare-swiper',
        component: () => import('@/views/PublicWelfare/PublicWelfareSwiper.vue')
      },
      {
        path: '/user-management',
        component: () => import('@/views/PublicWelfare/UserManagement.vue')
      },
      {
        path: '/admin-farm-article',
        component: () => import('@/views/InformationSystem/AdminFarmArticle.vue')
      },
      {
        path: '/information-system-admin-swiper',
        component: () => import('@/views/InformationSystem/InformationAdminSwiper')
      },
      {
        path: '/special-agricultural-products',
        component: () => import('@/views/InformationSystem/SpecialAgriculturalProducts.vue')
      },
      {
        path: '/business/directory',
        component: () => import('@/views/InformationSystem/BusinessDirectory.vue')
      },
      {
        path: '/complaint-advice',
        component: () => import('@/views/InformationSystem/ComplaintAdvice.vue')
      }
    ]
  }
];
