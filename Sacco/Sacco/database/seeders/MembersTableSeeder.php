<?php
namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;

class MembersTableSeeder extends Seeder
{


    public function run()
    {

        DB::table('members')->update([
            'created_at' => '2023-01-05 12:34:56',
        ]);

        DB::table('members')->insert([
            [
                'member_id' => 'M006',
                'name' => 'kyobe john',
                'username' => 'john',
                'password' => Hash::make('M006'),
                'email' => 'kyobejohn@gmail.com',
                'phone_number' => '756839483',
                'total_contributions' => '500000',
                'previous_loan_performance' => '60',
                'loan_progress' => null,
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M007',
                'name' => 'kabuye Musa',
                'username' => 'musa',
                'password' => Hash::make('M007'),
                'email' => 'kabuyemusa@gmail.com',
                'phone_number' => '756831283',
                'total_contributions' => '550000',
                'previous_loan_performance' => '60',
                'loan_progress' => null,
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M008',
                'name' => 'mukisa kenneth',
                'username' => 'kenneth',
                'password' => Hash::make('M008'),
                'email' => 'mukisakenneth@gmail.com',
                'phone_number' => '756847483',
                'total_contributions' => '1550000',
                'previous_loan_performance' => '90',
                'loan_progress' => null,
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M009',
                'name' => 'naomi kankya',
                'username' => 'kankya',
                'password' => Hash::make('M009'),
                'email' => 'naomikankya@gmail.com',
                'phone_number' => '756889483',
                'total_contributions' => '950000',
                'previous_loan_performance' => '20',
                'loan_progress' => null,
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M010',
                'name' => 'kikanzi ashiraf',
                'username' => 'ashiraf',
                'password' => Hash::make('M010'),
                'email' => 'kikanziashiraf@gmail.com',
                'phone_number' => '756094483',
                'total_contributions' => '3950000',
                'previous_loan_performance' => '50',
                'loan_progress' => null,
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M011',
                'name' => 'namataka patricia',
                'username' => 'patricia',
                'password' => Hash::make('M011'),
                'email' => 'namatakapatricia@gmail.com',
                'phone_number' => '756839420',
                'total_contributions' => '4766000',
                'previous_loan_performance' => '90',
                'loan_progress' => '30',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M012',
                'name' => 'assimwe precious',
                'username' => 'precious',
                'password' => Hash::make('M012'),
                'email' => 'assimweprecious@gmail.com',
                'phone_number' => '756123483',
                'total_contributions' => '2500000',
                'previous_loan_performance' => '10',
                'loan_progress' => '50',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M013',
                'name' => 'higenyi yurri',
                'username' => 'yurri',
                'password' => Hash::make('M013'),
                'email' => 'higenyiyurri@gmail.com',
                'phone_number' => '706739483',
                'total_contributions' => '5000000',
                'previous_loan_performance' => '59',
                'loan_progress' => '78',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M014',
                'name' => 'owembabazi jane',
                'username' => 'jane',
                'password' => Hash::make('M014'),
                'email' => 'owembabazijane@gmail.com',
                'phone_number' => '709739483',
                'total_contributions' => '1350000',
                'previous_loan_performance' => '69',
                'loan_progress' => '76',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M015',
                'name' => 'ojok Amos',
                'username' => 'amos',
                'password' => Hash::make('M015'),
                'email' => 'ojokamosn@gmail.com',
                'phone_number' => '712839483',
                'total_contributions' => '4550000',
                'previous_loan_performance' => '35',
                'loan_progress' => '79',
                'created_at' => now(),
                'updated_at' => now()
            ],

        ]);
    }
}
