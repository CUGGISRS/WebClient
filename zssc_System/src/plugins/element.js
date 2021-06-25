import Vue from "vue";
import {
  Button,
  Message,
  MessageBox,
  Form,
  FormItem,
	Input,
	Row,
	Col,
	Tabs,
	TabPane,
	Radio,
	RadioGroup,
	DatePicker,
	Upload,
	Dialog,
	Container,
  Header,
  Aside,
	Main,
	Footer,
	Menu,
	Submenu,
	MenuItemGroup,
	MenuItem,
	Tooltip,
	Breadcrumb,
	BreadcrumbItem,
	Card,
	Table,
	TableColumn,
	Pagination,
	Image,
	Option,
	Select,
	Tag
} from "element-ui";

Vue.use(Button);
Vue.use(Form);
Vue.use(FormItem);
Vue.use(Input);
Vue.use(Row);
Vue.use(Col);
Vue.use(Tabs);
Vue.use(TabPane);
Vue.use(Radio);
Vue.use(RadioGroup);
Vue.use(DatePicker);
Vue.use(Upload);
Vue.use(Dialog);
Vue.use(Container);
Vue.use(Header);
Vue.use(Aside);
Vue.use(Main);
Vue.use(Footer);
Vue.use(Menu);
Vue.use(Submenu);
Vue.use(MenuItemGroup);
Vue.use(MenuItem);
Vue.use(Tooltip);
Vue.use(Breadcrumb);
Vue.use(BreadcrumbItem);
Vue.use(Card);
Vue.use(Table);
Vue.use(TableColumn);
Vue.use(Pagination);
Vue.use(Image);
Vue.use(Option);
Vue.use(Select);
Vue.use(Tag);

Vue.prototype.$message = Message;
Vue.prototype.$confirm = MessageBox.confirm;