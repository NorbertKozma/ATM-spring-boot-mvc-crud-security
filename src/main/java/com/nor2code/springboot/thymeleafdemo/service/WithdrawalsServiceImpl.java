package com.nor2code.springboot.thymeleafdemo.service;

import com.nor2code.springboot.thymeleafdemo.dao.MembersRepository;
import com.nor2code.springboot.thymeleafdemo.dao.UserRepository;
import com.nor2code.springboot.thymeleafdemo.dao.WithdrawalsRepository;
import com.nor2code.springboot.thymeleafdemo.entity.Members;
import com.nor2code.springboot.thymeleafdemo.entity.User;
import com.nor2code.springboot.thymeleafdemo.entity.Withdrawals;
import com.nor2code.springboot.thymeleafdemo.util.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WithdrawalsServiceImpl implements WithdrawalsService{

    private final int DAILY_LIMIT = 150000;

    @Autowired
    private WithdrawalsRepository withdrawalsRepository;

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public String withdraw(String userId, int amount) {

        Members member = membersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Integer totalToday = withdrawalsRepository.getTotalWithdrawalsForToday(userId);

        if (totalToday == null) {
            totalToday = 0;
        }

        if (totalToday + amount > DAILY_LIMIT) {
            return "Daily withdrawal limit exceeded (150000 Ft).";
        }

        User user = member.getUser();
        if (user.getAmount() < amount) {
            return "Insufficient funds";
        }

        // Levonjuk az összeget
        user.setAmount(user.getAmount() - amount);

        // Menteni a User-t

        userRepository.save(user);

        // Mentjük a tranzakciót
        Withdrawals withdrawal = new Withdrawals();
        withdrawal.setAmount(amount);
        //withdrawal.setUserId(userId);
        withdrawal.setMembers(member);
        withdrawal.setCreatedAt(LocalDateTime.now());


        withdrawalsRepository.save(withdrawal);

        // return "Withdrawal successful: " + FormatUtils.formatAmount(amount, 0) + " HUF\n\nYour new balance is:\n\n" + FormatUtils.formatAmount(user.getAmount(), 0) + " HUF";
        return "Withdrawal successful\n\nYour new balance is:\n\n" + FormatUtils.formatAmount(user.getAmount(), 0) + " HUF";
    }
}
