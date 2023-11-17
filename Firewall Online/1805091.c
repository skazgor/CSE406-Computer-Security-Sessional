#include <linux/kernel.h>
#include <linux/module.h>
#include <linux/netfilter.h>
#include <linux/netfilter_ipv4.h>
#include <linux/ip.h>
#include <linux/tcp.h>
#include <linux/udp.h>
#include <linux/icmp.h>
//#include <lin>
#include <linux/if_ether.h>
#include <linux/inet.h>


static struct nf_hook_ops hook1; 
int count[4]={0,0,0,0};


unsigned int blockOther(void *priv, struct sk_buff *skb,
                       const struct nf_hook_state *state)
{

   struct iphdr *iph;
   struct icmphdr *icmph;
   u32  src_ip_addr[4];

   char ip[16] = "10.9.0.1";
   
   char ip0[16] = "10.9.0.5";
   char ip1[16] = "192.168.60.5";
   char ip2[16] = "192.168.60.6";
   char ip3[16] = "192.168.60.7";
   
   in4_pton(ip0, -1, (u8 *)&src_ip_addr[0], '\0', NULL);
   in4_pton(ip1, -1, (u8 *)&src_ip_addr[1], '\0', NULL);
   in4_pton(ip2, -1, (u8 *)&src_ip_addr[2], '\0', NULL);
   in4_pton(ip3, -1, (u8 *)&src_ip_addr[3], '\0', NULL);
   
   
   u32  ip_addr;
   int j=0;

   if (!skb) return NF_ACCEPT;

   iph = ip_hdr(skb);
   in4_pton(ip, -1, (u8 *)&ip_addr, '\0', NULL);

   if (iph->protocol == IPPROTO_ICMP) {
       icmph = icmp_hdr(skb);
       if (iph->daddr == ip_addr && icmph->type == ICMP_ECHO){
       	 
       	 for (j=0;j<4;j++)
       	 {
       	 
       	 	if(iph->saddr == src_ip_addr[j])
       	 	{
	       	 	if(count[j]>2)
		       	 {
		       	 	printk(KERN_WARNING "*** Dropping %pI4 (ICMP)\n", &(iph->daddr));
			    		return NF_DROP;
		       	 }
		       	 else
		       	 {
		       	 	count[j] = count[j]+1 ;
		       	 }
	       	 	break;
       	 	}
       	 }   	 
            
        }
   }
   return NF_ACCEPT;
}




int registerFilter(void) {
   printk(KERN_INFO "Registering filters.\n");

   hook1.hook = blockOther;
   hook1.hooknum = NF_INET_LOCAL_IN;
   hook1.pf = PF_INET;
   hook1.priority = NF_IP_PRI_FIRST;
   nf_register_net_hook(&init_net, &hook1);


   return 0;
}

void removeFilter(void) {
   printk(KERN_INFO "The filters are being removed.\n");
   nf_unregister_net_hook(&init_net, &hook1);
}

module_init(registerFilter);
module_exit(removeFilter);

MODULE_LICENSE("GPL");

