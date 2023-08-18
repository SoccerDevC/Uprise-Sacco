<?php

namespace Database\Seeders;

use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;

class DepositSeeder extends Seeder

{
    public function run()
    {
        $deposits = [];

        $memberIds = DB::table('members')->pluck('member_id');

        foreach ($memberIds as $memberId) {
            $totalContributions = DB::table('members')
                ->where('member_id', $memberId)
                ->value('total_contributions');

            $deposit = [
                'receipt_number' => mt_rand(1000, 9999), // Random receipt number starting with 10**
                'member_id' => $memberId,
                'amount' => $totalContributions,
                'date' => rand(0, 1) === 0 ? $this->randomDate('2023-01-01', '2023-08-01') : $this->randomDate('2022-01-01', '2022-12-31'),
            ];

            $deposits[] = $deposit;

            // For half of the members, add a second deposit entry
            if (rand(0, 1) === 0) {
                $deposits[] = [
                    'receipt_number' => mt_rand(1000, 9999), // Random receipt number starting with 10**
                    'member_id' => $memberId,
                    'amount' => $totalContributions,
                    'date' => rand(0, 1) === 0 ? $this->randomDate('2023-01-01', '2023-08-01') : $this->randomDate('2022-01-01', '2022-12-31'),
                ];
            }
        }

        DB::table('deposits')->insert($deposits);
    }

    private function randomDate($startDate, $endDate)
    {
        $startTimestamp = strtotime($startDate);
        $endTimestamp = strtotime($endDate);
        $randomTimestamp = mt_rand($startTimestamp, $endTimestamp);
        return date('Y-m-d', $randomTimestamp);
    }
}