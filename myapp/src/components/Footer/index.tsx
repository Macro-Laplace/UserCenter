import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
const Footer: React.FC = () => {
  const defaultMessage = 'Modify by Macro-Laplace';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: '给Macro-Laplace发邮件',
          title: 'Mail',
          href: 'mailto:Macro-Laplace@outlook.com',
          blankTarget: true,
        },
        {
          key: '源码地址',
          title: <GithubOutlined />,
          href: 'https://github.com/Macro-Laplace/UserCenter',
          blankTarget: true,
        },
        {
          key: 'Ant Design',
          title: 'Ant Design',
          href: 'https://ant.design',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
